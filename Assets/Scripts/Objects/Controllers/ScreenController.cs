using System.Collections;
using System.Collections.Generic;
using UnityEngine;

using Info;

namespace ScreenObject
{
    /// <summary>
    /// Controls Screen containers
    /// </summary>
    public class ScreenController : AbstractController, IResizeable
    {
        /**Lists*/
        private List<ContainerController> containers;

        /** Public Vars */
        private double _margins;

        /// <summary>
        /// Sets the margins for the Screen
        /// </summary>
        /// <value></value>
        public double Margins
        {
            get { return _margins; }
            set
            {
                if (value <= 0) throw new System.ArgumentException("Margins cannot be less or equal to 0.");
                _margins = 1 - value;
            }
        }

        /// <summary>
        /// Sets the position of the whole screen; 1 is one screen away, 0 is base screen.
        /// </summary>
        /// <value></value>
        public int ScreenPosition
        {
            //-.5f to offset the set +.5f
            get { return (int)(X - .5f); }
            //+.5f because it needs to be centered.
            set { X = value + .5f; }
        }

        /// <summary>
        /// on start, set up screen
        /// </summary>
        private void Start()
        {
            //fills container array
            containers = new List<ContainerController>(GetComponentsInChildren<ContainerController>());
        }

        /// <summary>
        /// method to reformat screen and contents
        /// </summary>
        public void Resize()
        {

            Size = new Vector2(ScreenInfo.Width, ScreenInfo.Height);

            //sets container positions
            foreach(ContainerController container in containers)
            {
                //does it automatically
                container.Reposition();

                //sets scale based on lanscape vs portrait
                float value = (float)((ScreenInfo.Width * Margins) / container.Width);

                container.transform.localScale = new Vector2(value, value) / (ScreenInfo.IsPortrait ? 1 : 2);
            }
        }
    }
}