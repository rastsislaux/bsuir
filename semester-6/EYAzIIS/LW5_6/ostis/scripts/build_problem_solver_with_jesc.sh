#!/usr/bin/env bash
set -eo pipefail

if [ -z "${PLATFORM_PATH}" ];
then
  source "$(cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd)"/set_vars.sh
fi

"${APP_ROOT_PATH}/scripts/build_jesc_agents.sh"
"${PLATFORM_PATH}/scripts/build_sc_machine.sh" "$@"
