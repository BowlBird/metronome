namespace Overlay
{
    /// <summary>
    /// Controller for Overlay Object
    /// </summary>
    public class OverlayController : AbstractOverlayController
    {
        //keeps track of if more than one overlay is instantiated at a time.
        private static OverlayController previousOverlay;

        /// <summary>
        /// on creation of this object, destroy and resize
        /// </summary>
        override protected void Awake()
        {
            //calls parent
            base.Awake();

            //checks to see if it should destroy a previous overlay, then sets itself
            if(previousOverlay != null)
                previousOverlay.DestroyImmediately();
            previousOverlay = this;
        }
    }
}