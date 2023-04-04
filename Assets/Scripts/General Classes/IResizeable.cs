
namespace ScreenObject
{
    /// <summary>
    /// An interface that allows the resizing of it and it's contents (should be a container)
    /// </summary>
    public interface IResizeable
    {
        //method to resize itself, containers, or both
        /// <summary>
        /// Method to resize itself, containers, or both.
        /// </summary>
        void Resize();
    }
}
