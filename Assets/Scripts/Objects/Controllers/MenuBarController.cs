using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

using Info;

namespace ScreenObject
{
    /// <summary>
    /// Controls behavior of the menu bar.
    /// 
    /// Should be at the bottom of the screen always, on the right if in landscape.
    /// </summary>
    public class MenuBarController : AbstractController, IResizeable
    {
        /** Sprites To be passed*/
        [SerializeField] private Sprite bottomCurve;
        [SerializeField] private Sprite leftCurve;
        [SerializeField] private Sprite rightCurve;

        /// <summary>
        /// Nested Class Controller script for all 3 curves attached to the menu bar
        /// </summary>
        [RequireComponent(typeof(Image))]
        private class CurveController : AbstractController
        {
            /** Sprites */
            public static Sprite BottomCurve {get;set;}
            public static Sprite LeftCurve {get;set;}
            public static Sprite RightCurve {get;set;}

            /// <summary>
            /// Should be the smaller value
            /// </summary>
            public float SquareSize { get; set; }

            private int _type;
            /// <summary>
            /// Sets the type of Curve it is (0 = bottom, 1 = left, 2 = right)
            /// </summary>
            public int Type {
                get { return _type; }
                set { if (!(value >= 0 && value <= 2)) throw new System.ArgumentException("Type can only be between 0-2");
                _type = value;
                TypeUpdate();
                }
            }

            /// <summary>
            /// Updates the type of the curve, allowing for no needed deletion.
            /// </summary>
            private void TypeUpdate() {
                Image imageComponent = GetComponent<Image>();

                switch(Type) {
                    case 0: //bottom curve
                        Size = new Vector2(SquareSize * 2, SquareSize);
                        imageComponent.sprite = BottomCurve;
                        X = 0;
                        Y = 0;
                        PivotX = .5f;
                        PivotY = 0;
                        break;
                    case 1: //left curve
                        Size = new Vector2(SquareSize, SquareSize * 2);
                        imageComponent.sprite = LeftCurve;
                        X = 0;
                        Y = 1;
                        PivotX = 0;
                        PivotY = .5f;
                        break;
                    case 2: //right curve
                        Size = new Vector2(SquareSize, SquareSize * 2);
                        imageComponent.sprite = RightCurve;
                        X = 1;
                        Y = 1;
                        PivotX = 1;
                        PivotY = .5f;
                        break;
                }
            }
        }
        /** Lists */
        private List<ButtonController> buttons;

        /** Curve References */
        private CurveController curve1;
        private CurveController curve2;

        //controls height based on inspector input
        [SerializeField] private float height;
        [SerializeField] private float curveMultiplier;

        /// <summary>
        /// Initiates list
        /// </summary>
        private void Awake() {
            /** Initalize curves */
            CurveController.BottomCurve = bottomCurve;
            CurveController.LeftCurve = leftCurve;
            CurveController.RightCurve = rightCurve;

            curve1 = new GameObject().AddComponent<CurveController>().GetComponent<CurveController>();
            curve2 = new GameObject().AddComponent<CurveController>().GetComponent<CurveController>();
            
            GameObject[] curves = { curve1.gameObject, curve2.gameObject };
            foreach(GameObject curve in curves) {
                curve.name = "Menu Curve";
                curve.tag = "Foreground";
                curve.transform.SetParent(this.transform);
            }

            //initate the lists
            buttons = new List<ButtonController>(GetComponentsInChildren<ButtonController>());
        }

        /// <summary>
        /// Resizes the menu bar to fit the screen
        /// </summary>
        public void Resize() {

            /** Sets Size */
            Width = ScreenInfo.IsPortrait ? ScreenInfo.Width : ScreenInfo.Width / 2;

            // since bar should be the same height no matter what rotation
            // this uses the screen diagonal instead of pure height
            Height = ScreenInfo.Diagonal * height;

            Reposition();

            UpdateCurves();
        }

        /// <summary>
        /// Sets position
        /// </summary>
        private void Reposition() {
            
            /** Sets Position  */
            // X = (centered for portrait, right centered for landscape)
            // Y = (bottom of screen)
            X = ScreenInfo.IsPortrait ? .5f : .75f;
            Y = 0;

            /** Pixel size to allow resolution scaling */
            //sets it so that the bevels are roughly the same.
            GetComponent<Image>().pixelsPerUnitMultiplier = ScreenInfo.PixelMultiplier;

            /** Sets position of buttons */

            float spacing = 1f / (buttons.Count + 1);

            for (int i = 0; i < buttons.Count; i++)
            {
                ButtonController button = buttons[i];

                float position = spacing * (i + 1);

                button.X = position;
                button.Y = .5f;
            }
        }

        /// <summary>
        /// handles creation and destruction of curves
        /// </summary>
        private void UpdateCurves() {

            //sets size
            curve1.SquareSize = Height / 2 * curveMultiplier;
            curve2.SquareSize = Height / 2 * curveMultiplier;

            curve1.Type = ScreenInfo.IsPortrait ? 1 : 0;
            curve2.Type = 2;
        }
    }
}