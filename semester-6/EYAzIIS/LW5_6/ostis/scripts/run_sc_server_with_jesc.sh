#!/usr/bin/env bash
set -eo pipefail

if [ -z "${PLATFORM_PATH}" ];
then
  source "$(cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd)"/set_vars.sh
fi

"${PLATFORM_PATH}/scripts/run_sc_server.sh" "$@" &
pid1=$!

"${APP_ROOT_PATH}/scripts/run_jesc_agents.sh" "$@" &
pid2=$!

stop_pid2() {
  pkill -f "java.*jesc-agents"
}

trap stop_pid2 INT

wait

trap - INT
