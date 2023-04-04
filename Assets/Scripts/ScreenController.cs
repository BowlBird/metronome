using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class ScreenController : MonoBehaviour
{
    //margins
    private int xMargins = 30;
    private int yMargins = 200;

    //home menu containers
    public GameObject musicStaffContainer;
    public GameObject timeSignatureContainer;
    public GameObject subdivisionContainer;
    public GameObject bottomContainer;
    public GameObject menuSelector;

    //settings menu containers
    public GameObject soundsContainer;
    public GameObject spacingContainer;
    public GameObject trainingContainer;
    public GameObject resetContainer;

    void Start()
    {
        //creats container for all containers in home menu
        GameObject[] homeContainers =
        {
            musicStaffContainer,
            timeSignatureContainer, 
            subdivisionContainer, 
            bottomContainer
        };
        
        //creates container for all settings containers
        GameObject[] settingsContainers =
        {
            soundsContainer,
            trainingContainer,
            resetContainer,
            spacingContainer
        };
        

        //resizes containers and tab on side
        ResizeElements(homeContainers, xMargins, yMargins);
        ResizeElements(settingsContainers, xMargins, yMargins);

        ResizeTab(menuSelector, homeContainers[0].GetComponent<RectTransform>().localScale.x);
    }



    //helper methods

    //set position in screen space using a 0-1 scale
    public void SetPosition(GameObject g, float x, float y)
    {
        Vector2 screen = GetScreenSize();

        g.transform.position = new Vector3(x * screen.x, y * screen.y, g.transform.position.z);
    }

    //returns screen size as a vector2
    public static Vector2 GetScreenSize()
    {
        return new Vector2((int)Screen.width, (int)Screen.height);
    }

    //will do math to resize elements in a correct way
    public void ResizeElements(GameObject[] gArray, float xMargins, float yMargins) 
    {
        Vector2 screen = GetScreenSize();

        //reset the scale of each of the components
        foreach (GameObject g in gArray)
        {
            g.GetComponent<RectTransform>().localScale = Vector3.zero;
        }

        //scale components
        while (FindMaxWidth(gArray, xMargins) < screen.x && FindMaxHeight(gArray, yMargins, 0) < screen.y)
        {
            foreach (GameObject g in gArray)
            {
                Vector3 currentV3 = g.GetComponent<RectTransform>().localScale;

                g.GetComponent<RectTransform>().localScale = new Vector3(currentV3.x + .001f, currentV3.y + .001f, currentV3.z);
            }
        }

        //finds how much spacing should be inbetween components to use full screen

        float ySpacing = 0;

        //do if width was the limiting factor
        if(FindMaxWidth(gArray, xMargins) >= screen.x)
        {
            while(FindMaxHeight(gArray, yMargins, ySpacing) < screen.y)
            {
                ySpacing += 1;
            }
        }

        //now sets position
        float cumulativeScreenPosition = 1;

        float ySpacingScreenSpace = ySpacing / screen.y;

        float marginScreenSpace = yMargins / screen.y;

        for (int i = 0; i < gArray.Length; i++)
        {
            GameObject g = gArray[i];

            float gameObjectScreenHeight = (g.GetComponent<RectTransform>().rect.height * g.GetComponent<RectTransform>().localScale.y) / screen.y;

            cumulativeScreenPosition -= i == 0 ? gameObjectScreenHeight + marginScreenSpace: gameObjectScreenHeight + ySpacingScreenSpace;

            SetPosition(g, .5f, cumulativeScreenPosition);
        }
    }

    //puts the tab in the correct size and place
    private void ResizeTab(GameObject tabs, float scale)
    {
        tabs.GetComponent<RectTransform>().localScale = new Vector3(scale, scale, 0);
        tabs.GetComponent<RectTransform>().localPosition = new Vector3(-GetScreenSize().x / 2, 0);
    }

    //finds how much space the component takes up width wise
    private float FindMaxWidth(GameObject[] gArray, float margins)
    {
        float returnValue = 0;

        foreach(GameObject g in gArray)
        {
            RectTransform rt = g.GetComponent<RectTransform>();

            returnValue = Mathf.Max(returnValue, (rt.rect.width) * rt.localScale.x) + margins * 2;
        }

        return returnValue;
    }

    //finds how much space the components takes up height wise
    private float FindMaxHeight(GameObject[] gArray, float margins, float spacing)
    {
        float returnValue = 0;

        foreach(GameObject g in gArray)
        {
            returnValue += g.GetComponent<RectTransform>().rect.height * g.GetComponent<RectTransform>().localScale.y;

            //adds for each element
            returnValue += spacing;
        }

        //then subtracts so it is just inbetween the elements
        returnValue -= spacing;

        //add one last time to get the margin in
        returnValue += margins * 2;

        return returnValue;
    }
}
