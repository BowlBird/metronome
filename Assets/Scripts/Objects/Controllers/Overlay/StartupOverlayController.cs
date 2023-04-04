namespace Overlay
{
    /// <summary>
    /// An overlay only to be spawned on startup.
    /// It is the most basic implementation of an overlay possible.
    /// Only destroys a little earlier.
    /// </summary>
    public class StartupOverlayController : AbstractOverlayController
    {
        /// <summary>
        /// Destroys earlier than usual.
        /// </summary>
        override protected void Awake() {

            //set to destroy .5 seconds later, after animation has finished
            Destroy(gameObject, .5f);

            //sets width and height
            base.Resize();
        }
    }
}