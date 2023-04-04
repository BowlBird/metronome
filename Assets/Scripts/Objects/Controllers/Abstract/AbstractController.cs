using System.Collections;
using System.Collections.Generic;
using UnityEngine;

using Helper;

namespace ScreenObject
{
    /// <summary>
    /// An Abstract basic implementation of a screen object
    /// </summary>
    [RequireComponent(typeof(RectTransform),typeof(CanvasRenderer))]
    public abstract class AbstractController : MonoBehaviour
    {
        /// <summary>
        /// controls width, based on pixel size
        /// </summary>
        public virtual float Width
        {
            get { return transform.GetComponent<RectTransform>().sizeDelta.x; }
            set
            {
                if (value < 0) throw new System.ArgumentException("Illegal width value.");
                transform.GetComponent<RectTransform>().sizeDelta = new Vector2((float)value, Height);
            }
        }

        /// <summary>
        /// controls height, based on pixel size
        /// </summary>
        public virtual float Height
        {
            get { return transform.GetComponent<RectTransform>().sizeDelta.y; }
            set
            {
                if (value < 0) throw new System.ArgumentException("Illegal height value.");
                transform.GetComponent<RectTransform>().sizeDelta = new Vector2(Width, value);
            }
        }

        /// <summary>
        /// controls size based on pixel size (width and height)
        /// </summary>
        public virtual Vector2 Size
        {
            get { return transform.GetComponent<RectTransform>().sizeDelta; }
            set
            {
                if(value.x < 0 || value.y < 0) throw new System.ArgumentException("At least one component less than 0.");

                Width = value.x;
                Height = value.y;
            }
        }

        /// <summary>
        /// Sets X Value based on percent of screen size
        /// </summary>
        /// <value></value>
        public virtual float X
        {
            get { return Position.GetLocalPosition(transform.parent.GetComponent<AbstractController>(), transform.localPosition).x; }
            set { transform.localPosition = Position.SetLocalPosition(transform.parent.GetComponent<AbstractController>(), new Vector2(value, Y));}
        }

        /// <summary>
        /// Sets Y Value based on percent of screen size
        /// </summary>
        /// <value></value>
        public virtual float Y
        {
            get { return Position.GetLocalPosition(transform.parent.GetComponent<AbstractController>(), transform.localPosition).y; }
            set { transform.localPosition = Position.SetLocalPosition(transform.parent.GetComponent<AbstractController>(), new Vector2(X, value)); }
        }

        /// <summary>
        /// Reference to Pivot X of object
        /// </summary>
        public virtual float PivotX {
            get { return GetComponent<RectTransform>().pivot.x; }
            set { 
                if(value < 0 || value > 1) throw new System.ArgumentException("Must be between 0 - 1");
                GetComponent<RectTransform>().pivot = new Vector2(value, PivotY); 
            }
        }

        /// <summary>
        /// Reference to Pivot Y of object
        /// </summary>
        public virtual float PivotY {
            get { return GetComponent<RectTransform>().pivot.y; }
            set { 
                if(value < 0 || value > 1) throw new System.ArgumentException("Must be between 0 - 1");
                GetComponent<RectTransform>().pivot = new Vector2(PivotX, value); 
            }
        }
    }
}