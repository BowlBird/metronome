using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

using ScreenObject;

namespace Manager
{
    /// <summary>
    /// Initiates variables that need to be loaded.
    /// </summary>
    public class InitiatorManager : MonoBehaviour
    {
        /** controls color of application on startup */

        [SerializeField] private Color highlight;
        [SerializeField] private Color foreground;
        [SerializeField] private Color background;

        [Space(10)]

        [SerializeField, Min(0)]
        private int startScreen;

        /// <summary>
        /// Initializes program values
        /// </summary>
        public void Start()
        {
            //sets colors
            ColorManager.REFERENCE.Background = background;
            ColorManager.REFERENCE.Foreground = foreground;
            ColorManager.REFERENCE.Highlight = highlight;

            //sets screen
            DisplayController.REFERENCE.CurrentScreen = startScreen;
        }
    }
}