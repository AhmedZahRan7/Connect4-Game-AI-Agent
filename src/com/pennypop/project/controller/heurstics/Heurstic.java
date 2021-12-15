package com.pennypop.project.controller.heurstics;

import com.pennypop.project.GUI.connect4board.InternalBoard;

public interface Heurstic {
    int evaluate(InternalBoard board);
}
