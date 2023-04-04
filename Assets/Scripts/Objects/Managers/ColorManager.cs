using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;
using TMPro;

/// <summary>
/// Manager for the Color in the Application
/// </summary>
namespace Manager
{
    /// <summary>
    /// Manages the color of all objects in the scene using Tags.
    /// </summary>
    public class ColorManager : MonoBehaviour
    {
        /** Sets Up Singleton Reference */
        public static ColorManager REFERENCE { get; private set; }

        /** Public Vars */
        private Color background;
        private Color foreground;
        private Color highlight;

        /// <summary>
        /// Sets the Background color
        /// </summary>
        /// <value></value>
        public Color Background
        {
            get { return background; }
            set
            {
                background = value;
                UpdateColors("Background", value);
            }
        }

        /// <summary>
        /// Sets the Foreground color
        /// </summary>
        /// <value></value>
        public Color Foreground
        {
            get { return foreground; }
            set
            {
                foreground = value;
                UpdateColors("Foreground", value);
            }
        }

        /// <summary>
        /// Sets the Highlight color
        /// </summary>
        /// <value></value>
        public Color Highlight
        {
            get { return highlight; }
            set
            {
                highlight = value;
                UpdateColors("Highlight", value);
            }
        }

        /// <summary>
        /// Sets up Color Manager
        /// </summary>
        void Awake()
        {
            //sets singleton to this
            REFERENCE = this;
        }

        /// <summary>
        /// Updates colors
        /// </summary>
        /// <param name="tag"></param>
        /// <param name="color"></param>
        private void UpdateColors(string tag, Color color)
        {
            foreach (GameObject element in GameObject.FindGameObjectsWithTag(tag))
            {
                //if it has an image component
                if(element.GetComponent<Image>() != null)
                    element.GetComponent<Image>().color = color;
                else if(element.GetComponent<TextMeshProUGUI>() != null)
                    element.GetComponent<TextMeshProUGUI>().color = color;
                else
                    throw new System.Exception("Can't set color.");
            }
        }

        /// <summary>
        /// public method to manually update colors
        /// </summary>
        public void UpdateColors() {
            Foreground = foreground;
            Background = background;
            Highlight = highlight;
        }
    }
}