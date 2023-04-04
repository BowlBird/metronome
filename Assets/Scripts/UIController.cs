using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class UIController : MonoBehaviour
{
    public BeatController BeatController;
    public Text BPMText;
    public Slider BPMSlider;

    public Slider volumeSlider;
    public Toggle PauseToggle;

    public Text numerator;
    public Text denominator;
    public Text accentNumerator;
    public Text accentDenominator;

    public GameObject periodClock;
    public GameObject beatClock;
    public GameObject subdivisionClock;

    public GameObject homeMenu;
    public GameObject settingsMenu;

    public GameObject wholeNote;
    public GameObject halfNote;
    public GameObject quarterNote;
    public GameObject eighthNote;
    public GameObject sixteenthNote;
    public GameObject thirtySecondNote;
    public GameObject sixtyFourthNote;
    public GameObject oneHundredTwentyEighthNote;

    public GameObject noteHolder;
    public GameObject scrollRect;

    public GameObject bar;
    public GameObject endOfMeasureBar;

    public GameObject numberHolder;
    public GameObject numberText;
    private ArrayList currentNumbers;

    private int noteSpacing;
    private float currentNoteSpacing;
    private ArrayList currentBars;

    public GameObject tripletHolder;
    public GameObject tripletIndicator;
    private ArrayList currentTriplets;

    public GameObject indicator;
    public Toggle followDotToggle;

    public AudioClip[] sounds;
    public Dropdown accentedDropdown;
    public Dropdown defaultDropdown;
    public Dropdown subdivisionDropdown;

    private float lastTime;
    private ArrayList taps;

    public InputField bpmField;
    public InputField measuresField;
    public InputField untilField;
    public Toggle startToggle;
    public Text startToggleText;

    public Text randomMuteText;
    public Slider randomMuteSlider;

    //vars to work with
    private int numOfNotes;
    private int kindOfNote;
    private bool triplet;

    public void Start()
    {
        //instantates arrayList
        currentBars = new ArrayList();
        currentNumbers = new ArrayList();
        currentTriplets = new ArrayList();
        taps = new ArrayList();

        //sets note spacing 
        noteSpacing = 350;

        //sets default menu to home
        ShowMenu(homeMenu, true);
        ShowMenu(settingsMenu, false);

        //defaults volume
        ChangeVolume(volumeSlider.value);

        //starts Notes
        UpdateNotes();
    }



    //helper methods

    //handles BPM SLIDER ONLY (needed because unity editor can't use vars)
    public void HandleSliderUpdate()
    {
        //does math to see how much to add to bpm slider
        ChangeBPM(Mathf.RoundToInt(BPMSlider.value - BeatController.GetBPM()));
        RestartMetronome();
        ResetTrainer();
    }

    //handles scroll rect
    public void HandleScrollRectUpdate()
    {
        //sets lower bound
        if (scrollRect.GetComponent<RectTransform>().anchoredPosition.x > 0)
            scrollRect.GetComponent<RectTransform>().anchoredPosition = new Vector3(0, scrollRect.GetComponent<RectTransform>().anchoredPosition.y);
    }

    //method to reset scrollRect to default Position
    private void ResetScrollRect()
    {
        scrollRect.GetComponent<RectTransform>().anchoredPosition = new Vector3(0, 0, 0);
    }

    //handles play and pause button
    public void ChangePlayState()
    {
        if(PauseToggle.isOn)
        {
            BeatController.StartAudio();
        }
        else
        {
            BeatController.StopAudio();
            ResetTrainer();
        }
        HandleClocks();
        ResetIndicator();
        scrollRect.transform.parent.GetComponent<ScrollRect>().horizontal = true;
    }

    //handles beats per measure buttons
    public void ChangeBeatsPerMeasure(int amount)
    {
        BeatController.SetBeatsPerMeasure(BeatController.GetBeatsPerMeasure() + amount);

        numerator.text = BeatController.GetBeatsPerMeasure().ToString();
        accentNumerator.text = BeatController.GetBeatsPerMeasure().ToString();

        UpdateNotes();
    }

    //handles division changing buttons
    public void ChangeDivision(bool increment)
    {
        BeatController.SetDivision(Mathf.RoundToInt(BeatController.GetDivision() * (increment ? 2f : .5f)));

        denominator.text = BeatController.GetDivision().ToString();
        accentDenominator.text = BeatController.GetDivision().ToString();

        UpdateNotes();
    }

    //handles changes to BPM
    public void ChangeBPM(int amount)
    {
        //changes BPM
        int newBPM = BeatController.GetBPM() + amount;

        BeatController.SetBPM(newBPM);

        //sets text
        SetBPMText(BeatController.GetBPM().ToString());

        //slider value doesn't change automatically so it needs to be changed too
        BPMSlider.value = newBPM;
    }

    //sets bpmText
    public void SetBPMText(string s)
    {
        BPMText.text = s;
    }

    //handles the changes to subdivisions
    public void ChangeSubdivisions(int amount)
    {
        BeatController.SetSubdivisions(amount);
        UpdateNotes();
    }

    //aligns clocks to bpm and starts them
    private void HandleClocks()
    {
        //checks if speed should be one or zero
        periodClock.GetComponent<Animator>().speed = PauseToggle.isOn ? 1 : 0;
        beatClock.GetComponent<Animator>().speed = PauseToggle.isOn ? 1 : 0;
        subdivisionClock.GetComponent<Animator>().speed = PauseToggle.isOn ? 1 : 0;

        float beatTime = (BeatController.GetBPM() / 60f) * (BeatController.GetDivision() / 4);
        periodClock.GetComponent<Animator>().Play("Idle",0,0f);
        periodClock.GetComponent<Animator>().SetFloat("speedController", beatTime / BeatController.GetBeatsPerMeasure());

        beatClock.GetComponent<Animator>().Play("Idle", 0, 0f);
        beatClock.GetComponent<Animator>().SetFloat("speedController", beatTime);

        subdivisionClock.GetComponent<Animator>().Play("Idle", 0, 0f);
        subdivisionClock.GetComponent<Animator>().SetFloat("speedController", beatTime * (BeatController.GetSubdivisions() + 1));
    }

    //made for the slider for the volume method
    public void ChangeVolumeFromSlider()
    {
        ChangeVolume(volumeSlider.value);
    }

    //changes the volume of the beats
    public void ChangeVolume(float volume)
    {
        BeatController.SetAudioMultiplier(volume);
    }

    //changes the screen depending on the toggle
    public void ChangeScreens(GameObject g)
    {
        if (g.GetComponent<Toggle>().isOn)
        {
            if (g.name == "Home Toggle")
            {
                ShowMenu(homeMenu, true);
                ShowMenu(settingsMenu, false);
            }
            else if (g.name == "Settings Toggle")
            {
                ShowMenu(homeMenu, false);
                ShowMenu(settingsMenu, true);
            }
        }
    }

    //method to help rest of buttons
    public void RestartMetronome()
    {
        if (PauseToggle.isOn)
        {
            BeatController.StartAudio();
        }
        HandleClocks();
    }

    //helper method for showing menus
    private void ShowMenu(GameObject g, bool b)
    {
        if(b)
        {
            g.GetComponent<RectTransform>().localPosition = Vector3.zero;
        }
        else
        {
            g.GetComponent<RectTransform>().localPosition = new Vector3(0, 100000);

        }
    }

    //controls notes 
    public void UpdateNotes()
    {
        //original vars
        int beatsPerMeasure = BeatController.GetBeatsPerMeasure();
        int division = (int)BeatController.GetDivision();
        int subdivisions = BeatController.GetSubdivisions();

        //vars to work with
        numOfNotes = 0;
        kindOfNote = 0;
        triplet = false;

        //logic to set numOfNotes
        numOfNotes = beatsPerMeasure * (subdivisions + 1);

        //resets bar
        ResetScrollRect();

        //logic to find the kind of note and triplet
        //1 = whole note, 2 = half note, 4 = quarter note, 8 = eighth note, 16 = sixteenth note, 32 = thirty-second note, 64 = sixty-fourth note
        switch(subdivisions)
        {
            case 0:
                kindOfNote = division;
                triplet = false;
                break;
            case 1:
                kindOfNote = division * 2;
                triplet = false;
                break;
            case 2:
                kindOfNote = division * 2;
                triplet = true;
                break;
            case 3:
                kindOfNote = division * 4;
                triplet = false;
                break;
        }

        //Destroys then respawns notes
        DestroyNotes();
        DestroyTriplets();
        SpawnNotes(numOfNotes, kindOfNote, subdivisions, noteSpacing);

        ResetTrainer();

        //makes it happen next frame
        Invoke("RestartMetronome", Time.deltaTime);
        Invoke("ResetEndOfMeasure", Time.deltaTime);
        Invoke("SpawnNumbers", Time.deltaTime);

        //do if triplets are the subdivision
        if(BeatController.GetSubdivisions() == 2)
        {
            Invoke("HandleTriplets", Time.deltaTime);
        }
    }

    private void SpawnNotes(int numOfNotes, int kindOfNote, int subdivisions, float spacing)
    {
        //gets correct note
        GameObject note = null;

        switch (kindOfNote)
        {
            case 1:
                note = wholeNote;
                spacing *= 2;
                break;
            case 2:
                note = halfNote;
                spacing *= 2;
                break;
            case 4:
                note = quarterNote;
                break;
            case 8:
                note = eighthNote;
                spacing /= 2;
                break;
            case 16:
                note = sixteenthNote;
                spacing /= 2;
                break;
            case 32:
                note = thirtySecondNote;
                spacing /= 2;
                break;
            case 64:
                note = sixtyFourthNote;
                spacing /= 2;
                break;
            case 128:
                note = oneHundredTwentyEighthNote;
                spacing /= 2;
                break;
        }

        //for loop to spawn correct amount of notes
        for (int i = 0; i < numOfNotes; i++)
        {
            GameObject spawnedNote = Instantiate(note, noteHolder.transform);

            //sets position of notes
            spawnedNote.GetComponent<RectTransform>().localPosition = new Vector3(spacing * i, 0, 0);

            //sets accented or not
            if (i == 0)
                spawnedNote.GetComponent<NoteController>().SetMode(2);
            else 
                spawnedNote.GetComponent<NoteController>().SetMode(1);

            //sets if note is a subdivision or not
            if(i % (subdivisions + 1) != 0)
            {
                spawnedNote.GetComponent<NoteController>().SetIsSubdivision(true);
            }
        }

        currentNoteSpacing = spacing;
        //invoked so there is the proper number of children
        Invoke("SpawnBars", Time.deltaTime);
    }

    //destroys notes
    private void DestroyNotes()
    {
        DestroyBars();
        int numOfChildren = noteHolder.transform.childCount;

        //destroys all notes under parent
        for(int i = 0; i < numOfChildren; i++)
        {
            Destroy(noteHolder.transform.GetChild(i).gameObject);
        }

    }

    public void DestroyBars()
    {
        //destroys bars
        foreach (GameObject bar in currentBars)
        {
            Destroy(bar);
        }
        currentBars.Clear();
    }

    //spawns bars, and end of measure bar
    public void SpawnBars()
    {
        //gets note array
        GameObject[] notes = GetNoteArray();

        int division = (int)BeatController.GetDivision();

        //iterates and draws bar for each note
        for (int i = 0; i < notes.Length; i++)
        {
            float spacing = currentNoteSpacing;
            GameObject note = notes[i];

            //only do if you need bars
            if (kindOfNote >= 8)
            {
                //TRIPLETS ARE SPECIAL CASES TOO
                //special case, if there is only one note
                if (notes.Length == 1)
                {
                    spacing /= 2;
                }
                //if the note is a rest
                else if (note.GetComponent<NoteController>().GetMode() == 0)
                {
                    continue;
                }
                //if it isn't a rest
                else
                {
                    //if the note is on the edges
                    if (i == 0 || i + 1 == notes.Length)
                    {
                        //splits the two
                        //this is the first one
                        if (i == 0)
                        {
                            //if the next note is a rest
                            if (notes[i + 1].GetComponent<NoteController>().GetMode() == 0)
                            {
                                spacing /= 2;
                            }
                        }
                        //this is the last one
                        else
                        {
                            //if the note before is a rest
                            if (notes[i - 1].GetComponent<NoteController>().GetMode() == 0)
                            {
                                spacing /= 2;
                            }
                            //if the note before was end of beat
                            else if ((i % (kindOfNote / 4) == 0 && !triplet) || (NoteOfTripletBeat(i, notes, -1) && triplet))
                            {
                                spacing /= 2;
                            }
                            //otherwise, don't draw a line
                            else
                            {
                                continue;
                            }

                        }
                    }
                    //if it isn't on the edges
                    else
                    {
                        //if surrounding notes are rests
                        if (notes[i - 1].GetComponent<NoteController>().GetMode() == 0 && notes[i + 1].GetComponent<NoteController>().GetMode() == 0)
                        {
                            spacing /= 2;
                        }
                        //if note after is a rest only
                        else if (notes[i + 1].GetComponent<NoteController>().GetMode() == 0)
                        {
                            //checks if the last note was not connected because of the end of bars
                            if ((i % (kindOfNote / 4) == 0 && !triplet) || (NoteOfTripletBeat(i, notes, 0) && triplet))
                            {
                                spacing /= 2;
                            }
                            else
                            {
                                continue;
                            }
                        }
                    }
                }
                //this makes sure that beats are separated on the beat mark
                //if there is a rest before this, do it anyways
                //also don't do the starting note
                if ((i % (kindOfNote / 4) == (kindOfNote / 4) - 1 && !triplet) || (NoteOfTripletBeat(i, notes, -1) && triplet))
                {
                    if (i != 0)
                    {
                        if (!(notes[i - 1].GetComponent<NoteController>().GetMode() == 0))
                        {
                            continue;
                        }
                        else
                        {
                            spacing /= 2;
                        }
                    }
                }

                //brute force because logic is weird
                if (spacing < currentNoteSpacing / 2)
                {
                    spacing = currentNoteSpacing / 2;
                }

                //creates bar(s)
                for (int j = 0; j < Mathf.Log(kindOfNote, 2) - 2; j++)
                {
                    //instantiates bar
                    GameObject currentBar = Instantiate(bar, note.transform);

                    //sets scale
                    currentBar.transform.localScale = new Vector3(spacing, currentBar.transform.localScale.y);

                    //sets position; random numbers are to slightly adjust position
                    float noteWidth = note.GetComponent<RectTransform>().rect.width;
                    float noteHeight = note.GetComponent<RectTransform>().rect.height;
                    float currentBarHeight = currentBar.GetComponent<RectTransform>().rect.height;
                    Vector3 currentBarPosition = currentBar.transform.localPosition;
                    currentBar.transform.localPosition = new Vector3(currentBarPosition.x - noteWidth / 2 + 6.1f, currentBarPosition.y - noteHeight / 2 + 8.5f + (currentBarHeight * 2) * j);

                    //sets color
                    Color noteColor = note.transform.GetChild(0).GetComponent<Image>().color;
                    Color barColor = currentBar.GetComponent<Image>().color;

                    currentBar.GetComponent<Image>().color = new Color(barColor.r, barColor.g, barColor.b, noteColor.a);

                    //adds current bar to the arraylist
                    currentBars.Add(currentBar);
                }
            }
        }
    }

    //used to check triplet conditions
    //last number is used to indicate what beat to check for
    private bool NoteOfTripletBeat(int i, GameObject[] notes, int beatNum)
    {
        //checks for special cases like lines getting cut off
        if(i % 3 == (3 + beatNum) % 3)
        {
            //to iterate from (for example 12 to 6 to 3)
            for (int j = 0; j < Mathf.Log(kindOfNote >= 8 ? kindOfNote / 8 : 2, 2) + 1; j++)
            {
                //if the start of the beat, plus the subdivision were checking is within range
                if (i - i % ((3 * (kindOfNote >= 8 ? kindOfNote / 8 : 1)) / Mathf.Pow(2, j)) + ((3 * (kindOfNote >= 8 ? kindOfNote / 8 : 1)) / Mathf.Pow(2, j)) <= notes.Length)
                {
                    //if that division starts on this note
                    if((i + beatNum * -1) % ((3 * (kindOfNote >= 8 ? kindOfNote / 8 : 1)) / Mathf.Pow(2, j)) == 0)
                    {
                        return true;
                    } 
                    else
                    {
                        break;
                    }
                }
            }
        }
        return false;
    }

    //notes per triplet beat
    private int NotesPerTripletBeat(int i, GameObject[] notes)
    {
        //checks for special cases like lines getting cut off
        if (i % 3 == 0)
        {
            //to iterate from (for example 12 to 6 to 3), conditional statement makes the loop run at least once
            for (int j = 0; j < Mathf.Log(kindOfNote >= 8 ? kindOfNote / 8 : 2, 2) + 1; j++)
            {
                //if the start of the beat, plus the subdivision were checking is within range
                if (i - i % ((3 * (kindOfNote >= 8 ? kindOfNote / 8 : 1)) / Mathf.Pow(2, j)) + ((3 * (kindOfNote >= 8 ? kindOfNote / 8 : 1)) / Mathf.Pow(2, j)) <= notes.Length)
                {
                    //if that division starts on this note
                    if (i % ((3 * (kindOfNote >= 8 ? kindOfNote / 8 : 1)) / Mathf.Pow(2, j)) == 0)
                    {
                        return (int)((3 * (kindOfNote >= 8 ? kindOfNote / 8 : 1)) / Mathf.Pow(2, j));
                    }
                    else
                    {
                        break;
                    }
                }
            }
        }
        return 0;
    }

    //resets the end of measures position
    private void ResetEndOfMeasure()
    {
        //gets the gameobject
        GameObject eom = GameObject.Find("EndOfMeasure");

        GameObject[] notes = GetNoteArray();

        //sets parent so localposition can be used
        eom.transform.SetParent(notes[notes.Length-1].transform.parent, false);

        //sets position
        eom.transform.localPosition = new Vector3(notes[notes.Length - 1].GetComponent<RectTransform>().localPosition.x + currentNoteSpacing, eom.transform.localPosition.y);

        //resets parent so the get array method works
        eom.transform.SetParent(noteHolder.transform.parent, true);
    }

    public Note[] GetNotes()
    {
        GameObject[] noteArray = GetNoteArray();

        Note[] notes = new Note[noteArray.Length];

        for(int i = 0; i < noteArray.Length; i++)
        {
            notes[i] = new Note(null);

            switch (noteArray[i].GetComponent<NoteController>().GetMode())
            {
                case 1:
                    notes[i].clip = BeatController.GetSubdivisionHitSound();
                    break;
                case 2:
                    notes[i].clip = BeatController.GetDefaultHitSound();
                    break;
                case 3:
                    notes[i].clip = BeatController.GetAccentedHitSound();
                    break;
            }
        }
        return notes;
    }

    //returns note array
    public GameObject[] GetNoteArray()
    {
        GameObject[] noteArray = new GameObject[noteHolder.transform.childCount];

        for (int i = 0; i < noteHolder.transform.childCount;i++)
        {
            noteArray[i] = noteHolder.transform.GetChild(i).gameObject;
        }
        return noteArray;
    }

    private void SpawnNumbers()
    {
        //clears arraylist
        foreach(GameObject number in currentNumbers)
        {
            Destroy(number);
        }

        currentNumbers.Clear();

        //resets numbers
        GameObject[] notes = GetNoteArray();

        for(int i = 0; i < notes.Length / (BeatController.GetSubdivisions() + 1); i++)
        {
            GameObject number = Instantiate(numberText, numberHolder.transform);

            //sets position
            number.transform.localPosition = new Vector3(currentNoteSpacing * i * (BeatController.GetSubdivisions() + 1), 0);

            //sets text
            number.GetComponent<Text>().text = (i + 1).ToString();

            //adds number
            currentNumbers.Add(number);
        }
    }

    private void DestroyTriplets()
    {
        //resets arraylist 
        foreach (GameObject triplet in currentTriplets)
        {
            Destroy(triplet);
        }
        currentTriplets.Clear();
    }

    private void HandleTriplets()
    {
        DestroyTriplets();

        //gets notes
        GameObject[] notes = GetNoteArray();

        //goes through every note and checks if its the start of a beat
        for (int i = 0; i < notes.Length; i++)
        {
            int usedNotesPerBeat = NotesPerTripletBeat(i, notes);

            if (usedNotesPerBeat != 0)
            {

                float noteWidth = notes[i].GetComponent<RectTransform>().rect.width;

                //instantiates a triplet and adds it to the list
                GameObject UITriplet = Instantiate(tripletIndicator, tripletHolder.transform);
                currentTriplets.Add(UITriplet);

                //Sets Left Bar to correct position
                GameObject leftBar = UITriplet.transform.GetChild(1).gameObject;
                leftBar.transform.localPosition = new Vector3(notes[i].transform.localPosition.x - noteWidth / 2, leftBar.transform.localPosition.y);

                //Sets Right Bar to correct position
                GameObject rightBar = UITriplet.transform.GetChild(2).gameObject;
                rightBar.transform.localPosition = new Vector3(notes[i + usedNotesPerBeat - 1].transform.localPosition.x - noteWidth / 2, rightBar.transform.localPosition.y);

                //Sets Bottom Bar to correct position and scale
                GameObject bottomBar = UITriplet.transform.GetChild(0).gameObject;
                bottomBar.transform.localPosition = new Vector3(leftBar.transform.localPosition.x, bottomBar.transform.localPosition.y);
                bottomBar.GetComponent<RectTransform>().sizeDelta = new Vector2(rightBar.transform.localPosition.x - leftBar.transform.localPosition.x, bottomBar.GetComponent<RectTransform>().rect.height);

                //Sets Text position and sets text
                GameObject text = UITriplet.transform.GetChild(3).gameObject;
                text.transform.localPosition = new Vector3((leftBar.transform.localPosition.x + rightBar.transform.localPosition.x + rightBar.GetComponent<RectTransform>().rect.width) / 2, text.transform.localPosition.y);
                text.GetComponent<Text>().text = usedNotesPerBeat.ToString();
            }
        }
    }

    public void HandleIndicator()
    {
        //makes not invisible
        indicator.GetComponent<Image>().color = new Color(1, 1, 1, 1);

        GameObject[] notes = GetNoteArray();

        //range between notes
        float localDistance = 0;


        //check if there is more than one note in the array
        if (notes.Length != 1) {
            localDistance = notes[1].GetComponent<RectTransform>().localPosition.x - notes[0].GetComponent<RectTransform>().localPosition.x;
        }

        //max x value the indicator can go
        float localMaxDistance = notes[notes.Length - 1].GetComponent<RectTransform>().localPosition.x + localDistance;

        //checks if indicator can be moved farther, minus one so that there is a bit of give
        if (indicator.GetComponent<RectTransform>().localPosition.x + localDistance <= localMaxDistance - 1)
        {
            Vector3 indicatorPos = indicator.GetComponent<RectTransform>().localPosition;
            indicator.GetComponent<RectTransform>().localPosition = new Vector3(indicatorPos.x + localDistance, indicatorPos.y);
        } 
        //else, reset indicator
        else
        {
            indicator.GetComponent<RectTransform>().localPosition = new Vector3(0, indicator.GetComponent<RectTransform>().localPosition.y);
        }

        //now checks if the scrollrect should follow the dot or not
        if(followDotToggle.isOn)
        {
            //makes it so you can't move the bar if follow dot is on
            scrollRect.transform.parent.GetComponent<ScrollRect>().horizontal = false;

            scrollRect.transform.localPosition = new Vector3(-indicator.transform.localPosition.x + 960, scrollRect.transform.localPosition.y);
        }
        else
        {
            //makes it so you can't move the bar if follow dot is on
            scrollRect.transform.parent.GetComponent<ScrollRect>().horizontal = true;
        }
    }

    public void ResetIndicator()
    {
        GameObject[] notes = GetNoteArray();

        //range between notes
        float localDistance = 0;


        //check if there is more than one note in the array
        if (notes.Length != 1)
        {
            localDistance = notes[1].GetComponent<RectTransform>().localPosition.x - notes[0].GetComponent<RectTransform>().localPosition.x;
        }

        //sets the position
        indicator.GetComponent<RectTransform>().localPosition = new Vector3(0 - localDistance, indicator.GetComponent<RectTransform>().localPosition.y);

        //makes invisible so you can't see it shift oddly
        indicator.GetComponent<Image>().color = new Color(1,1,1,0);
    }

    public void ChangeSounds(int typeOfBeat)
    {
        //type of beat will be 0, 1, or 2 based on if it is subdivision, default, or accented
        switch(typeOfBeat)
        {
            case 0:
                BeatController.SetSubdivisionHitSound(sounds[subdivisionDropdown.value * 3 + 2]);
                break;
            case 1:
                BeatController.SetDefaultHitSound(sounds[defaultDropdown.value * 3 + 1]); ;
                break;
            case 2:
                BeatController.SetAccentedHitSound(sounds[accentedDropdown.value * 3]);
                break;
        }
        RestartMetronome();
    }

    //handles TapBPM
    public void HandleTapBPM()
    {
        float tapInterval = Time.time - lastTime;

        if (tapInterval < 2)
        {

            //changes bpm
            float newBPM = Mathf.RoundToInt(60 / tapInterval);

            //modulates newBPM so it is correct depending on the subdivision you have
            newBPM /= kindOfNote / 4f;

            //finally rounds to int
            newBPM = Mathf.RoundToInt(newBPM);

            //now adds bpm to array
            taps.Add(newBPM);

            //actually used var
            float usedBPM = 0;
            foreach(float tap in taps)
            {
                usedBPM += tap;
            }

            //takes the average
            usedBPM /= taps.Count;

            //now rounds that to int
            usedBPM = Mathf.RoundToInt(usedBPM);

            ChangeBPM(-BeatController.GetBPM() + (int)usedBPM);
        }
        else
        {
            //when no longer tapping the button, clear the arraylist
            taps.Clear();
        }
        //sets last time
        lastTime = Time.time;
    }

    //handle Training
    public void HandleTraining()
    {
        //handles whether this is on or off, and if there is input
        if(startToggle.isOn && measuresField.text != "" && bpmField.text != "")
        {
            int bpmIncrement = int.Parse(bpmField.text);
            int measures = int.Parse(measuresField.text);
            int untilBPM = int.Parse(untilField.text);

            if (bpmIncrement > 0 && bpmIncrement < 300 && measures > 0 && untilBPM > BeatController.GetBPM() && untilBPM <= 300) 
            {
                //sets text
                startToggleText.text = "Stop";

                RestartMetronome();
            }
            else
            {
                ResetTrainer();
            }
        }
        else
        {
            ResetTrainer();
        }
    }

    public bool GetTrainerOn()
    {
        return startToggle.isOn;
    }

    public int GetTrainerRepeats()
    {
        return int.Parse(measuresField.text);
    }

    public void SetTrainerState(bool b)
    {
        startToggle.isOn = b;
    }

    //increments bpm
    public void IncrementBPM()
    {
        BeatController.PlayTrainingSound();

        //checks if all of it will fit or not, if it doens't, just go until it hits the ceiling
        if (BeatController.GetBPM() + int.Parse(bpmField.text) <= int.Parse(untilField.text))
            ChangeBPM(int.Parse(bpmField.text));
        else
            ChangeBPM(int.Parse(untilField.text) - BeatController.GetBPM());
    }

    private void ResetTrainer()
    {
        startToggle.isOn = false;

        //sets text
        startToggleText.text = "Start";
    }


    //methods for handling the random mute
    private void SetRandomMuteText(int i)
    {
        randomMuteText.text = i.ToString() + "%";
    }

    public void HandleRandomMuteSliderUpdate()
    {
        SetRandomMuteText((int)randomMuteSlider.value);
    }

    public int GetRandomMuteChance()
    {
        return (int)randomMuteSlider.value;
    }

    public void HandleButtonReset()
    {
        HandleReset(false, 120, 4, 4, 0, 0, 1, 2, false, "", "", "", 0);
    }
    //methods for handling the reset button
    public void HandleReset(bool followDot, int bpm, int beatsPerMeasure, int division, int subdivision, int accentedSound, int defaultSound, int subdivisionSound, bool trainerState, string bpmFieldVar, string measuresFieldVar, string untilFieldVar, int muteSlider)
    {
        //resets the follow dot button
        followDotToggle.isOn = followDot;

        //resets bpm to 120
        ChangeBPM((int)-(BeatController.GetBPM() - bpm));

        //resets time signature to 4/4
        ChangeBeatsPerMeasure(-(BeatController.GetBeatsPerMeasure() - beatsPerMeasure));

        while(BeatController.GetDivision() != 1)
        {
            ChangeDivision(false);
        }
        while(BeatController.GetDivision() != division)
        {
            ChangeDivision(true);
        }

        //changes subdivisions back
        ChangeSubdivisions(subdivision);
        Invoke("UpdateNotes", Time.deltaTime);

        //resets sounds
        BeatController.SetAccentedHitSound(sounds[accentedSound]);
        BeatController.SetDefaultHitSound(sounds[defaultSound]);
        BeatController.SetSubdivisionHitSound(sounds[subdivisionSound]);

        //int division is on purpose
        accentedDropdown.value = accentedSound / 3;
        defaultDropdown.value = defaultSound / 3;
        subdivisionDropdown.value = subdivisionSound / 3;

        //resets training
        SetTrainerState(trainerState);
        HandleTraining();
        bpmField.text = bpmFieldVar;
        measuresField.text = measuresFieldVar;
        untilField.text = untilFieldVar;

        //resets random mute
        randomMuteSlider.value = muteSlider;
        HandleRandomMuteSliderUpdate();
    }
}
