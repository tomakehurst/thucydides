import net.thucydides.core.screenshots.BlurLevel
import net.thucydides.core.screenshots.ScreenshotDigest
import net.thucydides.core.util.EnvironmentVariables
import net.thucydides.core.util.MockEnvironmentVariables
import spock.lang.Specification

/**
 * A description goes here.
 * User: john
 * Date: 27/02/2014
 * Time: 8:21 AM
 */

class WhenRecordingUniqueScreenshots extends Specification {

    File screenshotsSourceDirectory
    EnvironmentVariables environmentVariables = new MockEnvironmentVariables()

    def setup() {
        screenshotsSourceDirectory = new File(Thread.currentThread().getContextClassLoader().getResource("screenshots").getPath());
    }

    def "should calculate identical screenshot filename for identical files"() {
        given:
            def sampleScreenshot1 = new File(screenshotsSourceDirectory, "amazon.png")
            def sampleScreenshot2 = new File(screenshotsSourceDirectory, "amazon.png")

            def digester = new ScreenshotDigest(environmentVariables, null);
        when:
            def digest1 = digester.forScreenshot(sampleScreenshot1)
            def digest2 = digester.forScreenshot(sampleScreenshot2)
        then:
            digest1 == digest2
    }

    def "should calculate different screenshot filename for different files"() {
        given:
            def sampleScreenshot1 = new File(screenshotsSourceDirectory, "google_page_1.png")
            def sampleScreenshot2 = new File(screenshotsSourceDirectory, "amazon.png")
            def digester = new ScreenshotDigest(environmentVariables, null);
        when:
            def digest1 = digester.forScreenshot(sampleScreenshot1)
            def digest2 = digester.forScreenshot(sampleScreenshot2)
        then:
            digest1 != digest2
    }

    def "should calculate different screenshot filename for blurred and unblurred screenshots"() {
        given:
            def sampleScreenshot1 = new File(screenshotsSourceDirectory, "amazon.png")
            def sampleScreenshot2 = new File(screenshotsSourceDirectory, "amazon.png")
        when:
            def digester = new ScreenshotDigest(environmentVariables, null);
            def digest1 = digester.forScreenshot(sampleScreenshot1)

            def blurredDigester = new ScreenshotDigest(environmentVariables, BlurLevel.HEAVY);
            def digest2 = blurredDigester.forScreenshot(sampleScreenshot2)
        then:
            digest1 != digest2
    }


    def "should calculate different screenshot filename for resized screenshots"() {
        given:
            def sampleScreenshot1 = new File(screenshotsSourceDirectory, "amazon.png")
            def sampleScreenshot2 = new File(screenshotsSourceDirectory, "amazon.png")
        when:
            def digester = new ScreenshotDigest(environmentVariables,null);
            def digest1 = digester.forScreenshot(sampleScreenshot1)

            environmentVariables.setProperty("thucydides.resized.image.width", "500");
            def digest2 = digester.forScreenshot(sampleScreenshot2)
        then:
            digest1 != digest2
    }

}