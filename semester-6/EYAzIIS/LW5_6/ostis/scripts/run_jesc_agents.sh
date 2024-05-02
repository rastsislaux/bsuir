#! /usr/bin/env bash

set -eo pipefail

if [ -z "${APP_ROOT_PATH}" ];
then
  source "$(cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd)"/set_vars.sh
fi

java -Dlog.level=info -jar "${APP_ROOT_PATH}/problem-solver/jesc/build/libs/jesc-agents-1.0-SNAPSHOT-all.jar"

wait
