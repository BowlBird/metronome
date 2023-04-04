using System.Collections;
using System.Collections.Generic;
using UnityEngine;

using Info;
using Helper;

namespace ScreenObject
{
    /// <summary>
    /// Controller for Container Objects
    /// </summary>
    public class ContainerController : AbstractController
    {
        [SerializeField] private Vector2 portraitScreenPosition, landscapeScreenPosition;

        /// <summary>
        /// Set as the position (0-1) that the container should be in in portrait mode
        /// </summary>
        /// <value></value>
        public Vector2 PortraitScreenPosition
        {
            get { return portraitScreenPosition; }
            set { portraitScreenPosition = value; }
        }

        /// <summary>
        /// Set as the position (0-1) that the container should be in in landscape mode
        /// </summary>
        /// <value></value>
        public Vector2 LandscapeScreenPosition
        {
            get { return landscapeScreenPosition; }
            set { landscapeScreenPosition = value; }
        }

        /// <summary>
        /// Sets Positions to exactly intended Position.
        /// </summary>
        public void Reposition() {
            X = ScreenInfo.IsPortrait ? PortraitScreenPosition.x : LandscapeScreenPosition.x;
            Y = ScreenInfo.IsPortrait ? PortraitScreenPosition.y : LandscapeScreenPosition.y;
        }
    }
}
