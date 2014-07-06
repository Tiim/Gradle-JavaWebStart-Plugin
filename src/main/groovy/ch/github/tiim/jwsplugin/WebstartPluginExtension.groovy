package ch.github.tiim.jwsplugin

/**
 * Every field which is documented with 'value' can be the object directly, a closure or a callable
 * @author Tim
 * @since 07 - 2014
 */
class WebstartPluginExtension {

    /**
     * Value.
     * Contains the path to the output file, relative to the build.gradle file.
     */
    def outputFile = null

    /**
     * Contains the default aoishdf //TODO this
     */
    def jarFiles = null

    /**
     * Value.
     * Specifies the main class of the application
     */
    def mainClass = null;

    /**
     * Value containing the JNLP spec version
     */
    def spec = "1.0"

    /**
     * Value containing the base location of all urls specified in href attributes
     */
    def codebase = null

    /**
     * Value.
     * The url of the jnlp itself.
     * (Relative to {@link #codebase})
     */
    def jnlpHref = null

    /**
     * Value.
     * The version of the application and the jnlp
     */
    def version = null

    /**
     * Value.
     * The title of the application
     */
    def title = null

    /**
     * Value.
     * The vendor of the application
     */
    def vendor = null

    /**
     * Value.
     * The hompage of the application
     */
    def homepage = null

    /**
     * Value.
     * A short statement describing the application as a String <br/>
     * or <br/>
     * A map with the following keys: <br/>
     * * one-line <br/>
     * * short <br/>
     * * tooltip <br/>
     * With the description as value.
     */
    def appDescription = null

    /**
     * A list containing a map containing the following keys: <br/>
     * * href (required) - link to the icon file (gif, jpg, png, ico) <br/>
     * * kind (optional) - the suggested use (default, selected, disabled, rollover, splash, or shortcut) <br/>
     * * width (optional) - Can be used to indicate the resolution of the image. <br/>
     * * height (optional) - Can be used to indicate the resolution of the image. <br/>
     * * debth (optional) - Can be used to indicate the resolution of the image. <br/>
     */
    def icon = []

    /**
     * Value.
     * Indicates that this application can operate when the client system is disconnected from the network.
     */
    def offlineAllowed = true

    /**
     * Value.
     * Versions of Java software to run the application with.
     */
    def javaVersion = null


    /**
     * Value.
     * The preference for when the JNLP client should check for updates. Value can be always, timeout, or background
     */
    def updateCheck = "timeout"

    /**
     * Value.
     * The preference for how the JNLP client should handle a application update when a new version is available before
     * the application is launched. Values can be always, prompt-update, or prompt-run.
     */
    def updatePolicy = "prompt-update"
}
