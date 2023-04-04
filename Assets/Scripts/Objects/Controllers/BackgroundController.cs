using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

using Info;

namespace ScreenObject
{
    /// <summary>
    /// Controller Script for the background object
    /// </summary>
    public class BackgroundController : AbstractController, IResizeable
    {
        /// <summary>
        /// Resizes the background to fit the screen.
        /// </summary>
        public void Resize()
        {
            X = .5f;
            Y = .5f;

            Width = ScreenInfo.Width;
            Height = ScreenInfo.Height;
        }
    }
}