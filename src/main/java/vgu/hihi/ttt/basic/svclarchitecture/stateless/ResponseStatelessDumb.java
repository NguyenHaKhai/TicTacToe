package vgu.hihi.ttt.basic.svclarchitecture.stateless;

import vgu.hihi.ttt.basic.GameState;
public record ResponseStatelessDumb(GameState state, String boardMessage) {
        public String toProtocolMessage() {
            return state.name() + "|" + boardMessage;
        }
    }