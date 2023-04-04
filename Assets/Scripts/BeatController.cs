using System.Collections;
using System.Collections.Generic;
using UnityEngine;

[RequireComponent(typeof(AudioSource))]
public class BeatController : MonoBehaviour
{
    public static AudioSource audioSource { get; set; }

    public AudioClip accentedHit;
    public AudioClip defaultHit;
    public AudioClip subdivisionHit;
    public AudioClip trainingSound;

    [SerializeField]
    private int bpm;
    [SerializeField]
    private int beatsPerMeasure;

    private float waitPeriod;
    private float defaultWaitPeriod;
    private float bpmMultiplier;
    private int subdivisions;
    private float period;
    private float audioMultiplier;

    public UIController UIController;

    private double timer;
    private int increment;
    private bool play;
    private Note[] notes;
    private GameObject[] noteArray;
    private float interval;
    private int repeats;
    private bool mute;

    private void Start()
    {
        //sets audio source
        audioSource = GetComponent<AudioSource>();

        //defaults bpmMultiplier
        bpmMultiplier = 1;

        //sets wait period
        defaultWaitPeriod = .1f;
        waitPeriod = defaultWaitPeriod;

    }



    private void Update()
    {
        if (play)
        {
            timer += Time.deltaTime;

            if (timer >= interval)
            {
                //sets dot to next position
                CallHandleIndicator();

                timer = 0;

                if (!mute || repeats == 0)
                {
                    switch(noteArray[increment].GetComponent<NoteController>().GetMode())
                    {
                        case 1:
                            PlayDefaultHit();
                            break;
                        case 2:
                            PlayAccentedHit();
                            break;
                        default:
                            if(noteArray[increment].GetComponent<NoteController>().GetIsSubdivision())
                                PlaySubdivisionHit();
                            break;
                    }
                }


                //also used for the trainer to increase accuracy
                if (increment == notes.Length - 1)
                {
                    increment = 0;
                    repeats++;
                    mute = Random.Range(1, 101) <= UIController.GetRandomMuteChance();
                    waitPeriod = defaultWaitPeriod;
                    //used to ding the bell and increment if necessary
                    if (UIController.GetTrainerOn() && repeats == UIController.GetTrainerRepeats())
                    {
                        RecalculateInterval();
                        waitPeriod = interval;
                        UIController.IncrementBPM();
                        UIController.SetTrainerState(true);
                        UIController.HandleTraining();
                        repeats = 0;
                    }
                }
                else
                {
                    increment++;
                }
            }
        }
    }


    //helper methods

    //logic to place beats
    public void StartAudio()
    {
        StopAudio();
        CancelInvoke();
        Invoke("SetPlayToTrue", waitPeriod);

        notes = UIController.GetNotes();
        noteArray = UIController.GetNoteArray();

        RecalculateInterval();

        //sets the period of the function
        period = notes.Length * interval;

        //makes the indicator work
        Invoke("CallResetIndicator", Time.deltaTime);

        //sets to arbitrary value so that the first beat immediately plays
        timer = 10000000;

    }

    public void RecalculateInterval()
    {
        //sets the interval between every beat
        interval = (60f / GetBPM() / bpmMultiplier) / (subdivisions + 1);
    }

    //method so that wait time is easily implemented using invoke
    private void SetPlayToTrue()
    {
        play = true;
    }

    //stops audio
    public void StopAudio()
    {
        play = false;
        timer = 0;
        increment = 0;
        repeats = 0;
    }

    //plays hard hit
    private void PlayAccentedHit()
    {
        audioSource.volume = 1 * audioMultiplier;
        audioSource.PlayOneShot(accentedHit);
    }
    public void PlayTrainingSound()
    {
        audioSource.volume = 1 * audioMultiplier;
        audioSource.PlayOneShot(trainingSound);
    }

    //plays normal hit
    private void PlayDefaultHit()
    {
        audioSource.volume = 1 * audioMultiplier;
        audioSource.PlayOneShot(defaultHit);
    }

    //plays soft hit
    private void PlaySubdivisionHit()
    {
        audioSource.volume = 1 * audioMultiplier;
        audioSource.PlayOneShot(subdivisionHit);
    }

    //method to call the other one in UIController for the sake of organization
    private void CallHandleIndicator()
    {
        UIController.HandleIndicator();
    }
    private void CallResetIndicator()
    {
        UIController.ResetIndicator();
    }

    //old fashioned getter/setter
    public float GetPeriod()
    {
        return period;
    }
    public float GetInterval()
    {
        return interval;
    }
    public void SetVolume(float volume)
    {
        audioSource.volume = volume;
    }

    public int GetBeatsPerMeasure()
    {
        return beatsPerMeasure;
    }

    public void SetBeatsPerMeasure(int beatsPerMeasure)
    {
        if(beatsPerMeasure > 0)
        {
            this.beatsPerMeasure = beatsPerMeasure;
        }
    }

    public int GetSubdivisions()
    {
        return subdivisions;
    }

    public void SetSubdivisions(int subdivisions)
    {
        this.subdivisions = subdivisions;
    }

    public float GetDivision()
    {
        return bpmMultiplier * 4;
    }

    public void SetDivision(int subdivisions)
    {
        if (subdivisions > 0 && subdivisions <= 32)
        {
            this.bpmMultiplier = subdivisions / 4f;
        }
    }

    public int GetBPM()
    {
        return bpm;
    }

    public void SetBPM(int bpm)
    {
        if (bpm < 1)
            this.bpm = 1;
        else if (bpm > 300)
            this.bpm = 300;
        else
            this.bpm = bpm;
    }

    public void SetAudioMultiplier(float volume)
    {
        if(volume >= 0 && volume <= 1)
            audioMultiplier = volume;
    }

    public void SetAccentedHitSound(AudioClip clip)
    {
        accentedHit = clip;
    }

    public void SetDefaultHitSound(AudioClip clip)
    {
        defaultHit = clip;
    }
    
    public void SetSubdivisionHitSound(AudioClip clip)
    {
        subdivisionHit = clip;
    }

    public AudioClip GetAccentedHitSound()
    {
        return accentedHit;
    }

    public AudioClip GetDefaultHitSound()
    {
        return defaultHit;
    }

    public AudioClip GetSubdivisionHitSound()
    {
        return subdivisionHit;
    }
}

