
using System.Collections;
using System.Collections.Generic;
using UnityEngine;

/// <summary>
/// Info namespace allows certain types of info to easily be obtained
/// </summary>
namespace Info
{
    /// <summary>
    /// Gives info about the screen
    /// </summary>
    public class ScreenInfo : MonoBehaviour
    {
        /** Instance Vars*/
        public static bool IsPortrait { get; private set; }
        public static bool IsLandscape { get; private set; }
        public static float Height { get; private set; }
        public static float Width { get; private set; }
        public static bool RotateEvent { get; private set; }
        public static bool ResolutionEvent { get; private set; }
        public static float Diagonal { get; private set; }
        public static float PixelMultiplier { get; private set; }
        private Rect previousSafeArea;
        private Rect previousResolution;

        /// <summary>
        /// Calls UpdateScreenVariables as to not have problems will incorSafeArea screen sizes
        /// </summary>
        private void Awake()
        {
            UpdateScreenVariables();
        }
        /// <summary>
        /// Checks Rotate Event every Frame
        /// </summary>
        private void Update()
        {

            //this will cause one frame of delay, however I think it is worth it.
            RotateEvent = !Screen.safeArea.Equals(previousSafeArea);

            Rect screenRect = new Rect(0, 0, Screen.width, Screen.height);
            ResolutionEvent = !screenRect.Equals(previousResolution);

            if (RotateEvent || ResolutionEvent)
            {
                UpdateScreenVariables();
                previousSafeArea = Screen.safeArea;
                previousResolution = screenRect;
            }
        }

        /// <summary>
        /// Updates all instance variables based on screen
        /// </summary>
        private void UpdateScreenVariables()
        {

            //if portrait
            IsPortrait = Screen.height > Screen.width;
            IsLandscape = !IsPortrait;

            //gets width and height
            Height = Screen.height;
            Width = Screen.width;

            //gets the diagonal of the screen
            Diagonal = Mathf.Sqrt(Mathf.Pow(Height, 2) + Mathf.Pow(Width, 2));

            //this is so bevels and other resolution dependant sprites render properly
            //used as baseline
            float note8Diagonal = 3291.6865f;
            PixelMultiplier = note8Diagonal / Diagonal;

        }
    }
}