using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class ApplicationSettings : MonoBehaviour
{
    public Text debugText;

    void Start()
    {
        //application settings
        Application.runInBackground = true;
        //Application.targetFrameRate = 60;
    }

    private void Update()
    {
        debugText.text = Mathf.RoundToInt(1.0f / Time.deltaTime).ToString();
    }
}
