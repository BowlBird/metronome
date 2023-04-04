using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class Note
{
    public AudioClip clip { get; set; }

    //constructor
    public Note(AudioClip clip)
    {
        this.clip = clip;
    }

    //plays sound
    public void Play()
    {
        if(clip != null)
            BeatController.audioSource.PlayOneShot(clip);
    }

}
