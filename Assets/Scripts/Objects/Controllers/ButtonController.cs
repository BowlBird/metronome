using System.Collections;
using System.Collections.Generic;
using UnityEngine;

using Info;

namespace ScreenObject
{
    public class ButtonController : AbstractController, IResizeable
    {
        [SerializeField] private float multiplier;

        /// <summary>
        /// Resizes button
        /// </summary>
        public void Resize() {
            /** Sets Size */

            // since bar should be the same height no matter what rotation
            // this uses the screen diagonal instead of pure height
            int diagonal = (int) Mathf.Sqrt(Mathf.Pow(ScreenInfo.Width, 2) + Mathf.Pow(ScreenInfo.Height, 2));

            Height = diagonal * multiplier;
            Width = diagonal * multiplier;
        }
    }
}
