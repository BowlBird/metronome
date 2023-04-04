using UnityEngine;

using Info;
using ScreenObject;


namespace Helper
{
    /// <summary>
    /// Helper Methods to allow for interchange of local position and percent based position system.
    /// </summary>
    public class Position : MonoBehaviour
    {
        /// <summary>
        /// and sets its position based on a percent based system of full screen
        /// </summary>
        /// <param name="pos"></param>
        /// <returns></returns>
        public static Vector3 SetAbsolutePosition(Vector2 pos)
        {
            return new Vector3((pos.x * ScreenInfo.Width) - (ScreenInfo.Width / 2), (pos.y * ScreenInfo.Height) - (ScreenInfo.Height / 2));
        }

        /// <summary>
        /// Returns the local position based on percents
        /// </summary>
        /// <param name="parent"></param>
        /// <param name="pos"></param>
        /// <returns></returns>
        public static Vector3 SetLocalPosition(AbstractController parent, Vector2 pos)
        {
            float pixelPositionX = pos.x * ScreenInfo.Width;
            float halfScreenSizeX = ScreenInfo.Width / 2;
            float localScreenRatioX = ScreenInfo.Width / parent.Width;
            float pivotX = parent.Width * (.5f - parent.PivotX);

            float pixelPositionY = pos.y * ScreenInfo.Height;
            float halfScreenSizeY = ScreenInfo.Height / 2;
            float localScreenRatioY = ScreenInfo.Height / parent.Height;
            float pivotY = parent.Height * (.5f - parent.PivotY);

            //takes into account pivot, along with the width and height of the local parent.
            return new Vector3((pixelPositionX - halfScreenSizeX) / localScreenRatioX + pivotX, (pixelPositionY - halfScreenSizeY) / localScreenRatioY + pivotY);
        }

        /// <summary>
        /// returns position as a percent
        /// </summary>
        /// <param name="pos"></param>
        /// <returns></returns>
        public static Vector3 GetAbsolutePosition(Vector3 pos)
        {
            return new Vector3((pos.x + (ScreenInfo.Width / 2)) / ScreenInfo.Width, (pos.y + (ScreenInfo.Height / 2)) / ScreenInfo.Height);
        }

        /// <summary>
        /// Returns the local percent given transform.localPosition
        /// </summary>
        /// <param name="parent"></param>
        /// <param name="pos"></param>
        /// <returns></returns>
        public static Vector3 GetLocalPosition(AbstractController parent, Vector3 pos)
        { 
            float pivotX = (.5f - parent.PivotX);
            float pivotY = (.5f - parent.PivotY);

            //takes into account local parent width along with the pivot point of the parent
            return new Vector3((pos.x + (parent.Width / 2)) / parent.Width - pivotX, (pos.y + (parent.Height / 2)) / parent.Height - pivotY);
        }
    }
}