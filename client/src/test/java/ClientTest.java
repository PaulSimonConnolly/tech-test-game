import com.test.challenge.Client;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Scanner.class)
public class ClientTest {

    @Spy
    PrintWriter printWriter = PowerMockito.mock(PrintWriter.class);
    ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    Scanner scanner;
    Scanner keyboardInput;
    Socket socket;
    Client objectUnderTest;

    @Before
    public void onSetup() {
        System.setOut(new PrintStream(outContent));
        scanner = PowerMockito.mock(Scanner.class);
        socket = PowerMockito.mock(Socket.class);
    }

    @Test
    public void convertStringTo2DArrayTest() {
        objectUnderTest = new Client(socket, scanner, printWriter, null);
        final String testData = "[[1,2,3], [4,5,6]]";
        String[][] multiArray = objectUnderTest.convert(testData);
        Assert.assertArrayEquals(new String[]{"1", "2", "3"}, multiArray[0]);
        Assert.assertArrayEquals(new String[]{"4", "5", "6"}, multiArray[1]);
    }

    @Test
    public void promptUserToInputNameTest() {
        keyboardInput = new Scanner("Homer");
        objectUnderTest = new Client(socket, scanner, printWriter, keyboardInput);
        objectUnderTest.promptUserToInputName();
        Mockito.verify(printWriter).println("USERNAME:Homer");
    }

    @Test
    public void play_opponentDisconnect_Test() throws Exception {
        scanner = new Scanner("OPPONENT_DISCONNECT");
        objectUnderTest = new Client(socket, scanner, printWriter, null);
        objectUnderTest.play();
        Assert.assertTrue(outContent.toString().contains("Opponent disconnected, YOU WIN!"));
    }

    @Test
    public void play_draw_Test() throws Exception {
        scanner = new Scanner("DRAW");
        objectUnderTest = new Client(socket, scanner, printWriter, null);
        objectUnderTest.play();
        Assert.assertTrue(outContent.toString().contains("The game is a draw"));
    }
}
