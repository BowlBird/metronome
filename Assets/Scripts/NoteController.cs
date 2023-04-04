using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class NoteController : MonoBehaviour
{
    //sprites to use
    public Sprite accentedNote;
    public Sprite normalNote;
    public Sprite rest;
    private Sprite[] sprites;

    //underlying button
    private GameObject button;

    //0 is rest, 1 is normal, 2 is accented
    private int mode;
    private bool isSubdivision;

    private void Awake()
    {
        //creates array for easy changing
        sprites = new Sprite[3];
        sprites[0] = rest;
        sprites[1] = normalNote;
        sprites[2] = accentedNote;

        //sets button
        button = transform.GetChild(0).gameObject;
    }

    //method to set mode
    public void SetMode(int mode)
    {
        //sets mode if conditions are right
        if (mode >= 0 && mode <= 2)
        {
            this.mode = mode;
            ChangeSprite(mode);
        }
    }

    //method to incrememnt mode
    public void IncrementMode()
    {
        //incrememnts unless greater than 2
        mode++;
        mode = mode > 2 ? 0 : mode;

        ChangeSprite(mode);
    }

    //getter method for mode
    public int GetMode()
    {
        return mode;
    }

    //getter for issubdivision
    public bool GetIsSubdivision()
    {
        return isSubdivision;
    }

    //setter for issubdivision
    public void SetIsSubdivision(bool isSubdivision)
    {
        this.isSubdivision = isSubdivision;

        //sets opacity based on subdivision status
        button.GetComponent<Image>().color = new Color(1,1,1, isSubdivision ? .5f : 1f);
    }

    public void ChangeSprite(int mode)
    {
        button.GetComponent<Image>().sprite = sprites[mode];
    }

    public void RestartAudio()
    {
        GameObject.Find("Canvas").GetComponent<UIController>().DestroyBars();
        GameObject.Find("Canvas").GetComponent<UIController>().SpawnBars();
        GameObject.Find("Canvas").GetComponent<UIController>().RestartMetronome();
    }
}
