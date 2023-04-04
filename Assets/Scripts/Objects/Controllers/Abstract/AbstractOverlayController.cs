using Info;
using ScreenObject;

namespace Overlay {

    /// <summary>
    /// A Class to be extended for Overlay Behavior
    /// </summary>
    public abstract class AbstractOverlayController : AbstractController {

        /// <summary>
        /// Provides basic functionality of the overlay
        /// </summary>
        virtual protected void Awake() {

            //set to destroy 1 second later, after animation has finished
            Destroy(gameObject, 1);

            //sets width and height
            Resize();

        }

        /// <summary>
        /// resizes the overlay to fit the entire screen
        /// Does not implement the IResizable interface as it is created and destroyed frequenetly
        /// And this would force some timing shenanigans to take place.
        /// </summary>
        protected void Resize()
        {
            Width = ScreenInfo.Width;
            Height = ScreenInfo.Height;
        }

        /// <summary>
        /// Destroys the overlay immediately
        /// </summary>
        public void DestroyImmediately() {
            Destroy(this.gameObject);
        }
    }
}