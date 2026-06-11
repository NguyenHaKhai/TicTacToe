#!/bin/bash

HOST=${2:-localhost}
PORT=${3:-1234}
CLIENTS=${1:-10000}
HOLD_SECONDS=${4:-60}

PIDS=()

cleanup() {
    echo
    echo "Stopping all test clients..."

    for pid in "${PIDS[@]}"; do
        kill "$pid" 2>/dev/null
    done

    wait 2>/dev/null
    echo "All test clients stopped."
    exit 0
}

trap cleanup INT TERM

echo "Starting $CLIENTS clients to $HOST:$PORT"
echo "Each client will stay connected for $HOLD_SECONDS seconds."
echo "Press Ctrl+C to stop early."
echo

for ((i = 1; i <= CLIENTS; i++)); do
    (
        # Open TCP connection and keep it alive for HOLD_SECONDS.
        # The server will think this is a connected client.
        sleep "$HOLD_SECONDS" | nc "$HOST" "$PORT" >/dev/null 2>&1
    ) &

    PIDS+=($!)

    if (( i % 100 == 0 )); then
        echo "Started $i clients..."
    fi
done

echo
echo "All $CLIENTS clients started."
echo "Waiting for them to finish..."

wait

echo "Test finished."
