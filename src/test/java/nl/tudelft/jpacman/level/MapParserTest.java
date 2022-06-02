package nl.tudelft.jpacman.level;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import java.io.IOException;

import nl.tudelft.jpacman.board.Square;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import nl.tudelft.jpacman.PacmanConfigurationException;
import nl.tudelft.jpacman.board.BoardFactory;
import nl.tudelft.jpacman.npc.Ghost;
import org.mockito.Mockito;
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MapParserTest {
    private MapParser mapParser;
    private final LevelFactory levelFactory = mock(LevelFactory.class);
    private final BoardFactory boardFactory = mock(BoardFactory.class);
    @BeforeEach
    void init() {
        mapParser = new MapParser(levelFactory, boardFactory);
    }

    @Test
    void testlevel() throws IOException {
        Mockito.when(boardFactory.createGround()).thenReturn(mock(Square.class));
        Mockito.when(boardFactory.createWall()).thenReturn(mock(Square.class));
        //对于此处，相当于是new一个对象，然后mock的参数设置为对象，
        Ghost ghost = Mockito.mock(Ghost.class);
        Mockito.when(levelFactory.createGhost()).thenReturn(ghost);
        //相当于这两行代码
        //Mockito.when(levelFactory.createGhost()).thenReturn(mock(Ghost.class));
        Pellet pellet = Mockito.mock(Pellet.class);
        Mockito.when(levelFactory.createPellet()).thenReturn(pellet);
        //Mockito.when(levelFactory.createPellet()).thenReturn(mock(Pellet.class));
        mapParser.parseMap("/TestMap.txt");
        //然后进行验证，
        Mockito.verify(boardFactory,Mockito.times(3)).createGround();
        Mockito.verify(boardFactory,Mockito.times(1)).createWall();
        Mockito.verify(levelFactory,Mockito.times(1)).createGhost();
        Mockito.verify(levelFactory,Mockito.times(1)).createPellet();
    }
    //当有错误的时候可以直接抛出异常，此处用两种方法，第一个assertThatThrownBy，另一种为assertThrows。
    @Test
    void notfindFile() {
        String file = "/notknowmap.txt";
        assertThatThrownBy(()->{ mapParser.parseMap(file); }).isInstanceOf(PacmanConfigurationException.class).hasMessage("Could not get resource for: "+file);
    }
    @Test
    void notRecongize() {
        assertThrows(PacmanConfigurationException.class, () -> mapParser.parseMap("/UnrecognizeMap.txt"));
    }
}
