package upe.process;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class UProcessModificationTest {

    @Mock
    private UProcess proc;

    @Test
    public void testModificator() throws Exception {
        try(UProcessModification m = new UProcessModification(proc)) {
            // Nothing to be done.
        }
        verify(proc, times(1)).inputStarts();
        verify(proc, times(1)).inputStops();
    }
}
