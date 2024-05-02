#! /usr/bin/env bash

set -eo pipefail

if [ -z "${APP_ROOT_PATH}" ];
then
  source "$(cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd)"/set_vars.sh
fi

original_dir=$(pwd)

cd "${APP_ROOT_PATH}/problem-solver/jesc"
./gradlew shadowJar

cd "$original_dir"
