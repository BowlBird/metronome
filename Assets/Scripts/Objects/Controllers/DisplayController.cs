using System.Collections;
using System.Collections.Generic;
using UnityEngine;

using Info;

namespace ScreenObject
{
    /// <summary>
    /// Controller for the Display Object
    /// </summary>
    public class DisplayController : AbstractController
    {
        /** Singleton Reference*/
        public static DisplayController REFERENCE;

        /** Lists */
        private List<IResizeable> resizeables;
        private List<ScreenController> screens;

        /** Public Fields */
        [SerializeField] private GameObject overlay;
        [SerializeField] private GameObject startupOverlay;
        private int _currentScreen;

        /// <summary>
        /// Sets the current Screen seen in the application
        /// </summary>
        /// <value></value>
        public int CurrentScreen
        {
            get { return _currentScreen; }
            set
            {
                if (value < screens.Count && value >= 0 && value != _currentScreen)
                {
                    _currentScreen = value;
                    SetCurrentScreen();
                }
            }
        }

        [SerializeField,Range(.001f, 1)] private double _margins;

        /// <summary>
        /// Sets the margin Value for the whole application
        /// </summary>
        /// <value></value>
        public double Margins
        {
            get { return _margins; }
            set
            {
                if (value <= 0) throw new System.ArgumentException("Margins cannot be less than or equal to 0.");
                _margins = 1 - value;
            }
        }

        /// <summary>
        /// Sets up display
        /// </summary>
        private void Awake()
        {

            //sets singleton
            REFERENCE = this;

            //checks for resizables in children
            resizeables = new List<IResizeable>(transform.GetComponentsInChildren<IResizeable>());

            //gets all screens in children
            screens = new List<ScreenController>(GetComponentsInChildren<ScreenController>());

            //sets margins
            foreach (ScreenController sc in screens)
                sc.Margins = Margins;

            //does what it says
            SpawnStartupOverlay();
        }

        /// <summary>
        /// Maintains size by checking for events that would change sizes
        /// </summary>
        private void Update()
        {
            //checks to see if it should resize screen
            if (ScreenInfo.RotateEvent || ScreenInfo.ResolutionEvent)
                Resize();
        }

        /// <summary>
        /// calls all resize methods in children
        /// </summary>
        public void Resize()
        {
            SpawnOverlay();

            //ALL RESIZEABLES, not just immediate children.
            foreach (IResizeable resizeable in resizeables)
                resizeable.Resize();

            //need to update so that containers position is not strange
            ResetScreensPosition();
        }

        /// <summary>
        /// updates screen to current
        /// </summary>
        private void SetCurrentScreen()
        {
            SpawnOverlay();

            //allows for a smoother animation.
            Invoke("ResetScreensPosition",.1f);
        }

        /// <summary>
        /// Checks and sets screen positions
        /// </summary>
        private void ResetScreensPosition()
        {
            //set positions of each of the screens
            for (int i = 0; i < screens.Count; i++)
                screens[i].ScreenPosition = i - CurrentScreen;
        }

        /// <summary>
        /// spawns an overlay
        /// </summary>
        private void SpawnOverlay() { Instantiate(overlay, Vector3.zero, Quaternion.identity, transform); }

        /// <summary>
        /// spawns startupoverlay
        /// </summary>
        private void SpawnStartupOverlay() {Instantiate(startupOverlay, Vector3.zero, Quaternion.identity, transform);}
    }
}