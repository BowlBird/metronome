using System.Collections;
using System.Collections.Generic;
using UnityEngine;

namespace ScreenObject
{

    /// <summary>
    /// Controller for any links
    /// </summary>
    public class LinkController : AbstractController
    {
        [SerializeField] private string text;

        /// <summary>
        /// Opens text as a link
        /// </summary>
        public void OpenLink()
        {
            Application.OpenURL(text);
        }

        /// <summary>
        /// Copies text to clipboard
        /// </summary>
        public void CopyToClipboard()
        {
            GUIUtility.systemCopyBuffer = text;
        }
    }
}
