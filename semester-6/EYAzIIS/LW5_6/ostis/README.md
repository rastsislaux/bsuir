<h1 align="center">OSTIS Legislation</h1>

[![license](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

## About

OSTIS Legislation is an ostis-system based on [**ostis-web-platform**](https://github.com/ostis-ai/ostis-web-platform) and designed with [OSTIS Technology](https://github.com/ostis-ai).

The OSTIS Legislation project aims to develop a personal legal assistant, providing individuals with a reliable and accessible source of legal information.

## Installation

For Ubuntu/Debian based distros:
```sh
git clone https://github.com/ostis-apps/ostis-example-app.git
cd ostis-example-app
./scripts/install.sh
```

## Build knowledge base

Before first launch or after changes in KB you should build knowledge base. 

```sh
./scripts/build_kb.sh
```

## Usage

To launch system you should start sc-server:
```sh
./scripts/run_sc_server.sh
```

After that launch sc-web interface:

```sh
./scripts/run_sc_web.sh
```

To check that everything is fine open localhost:8000 in your browser.
![](https://i.imgur.com/zOQ7H6U.png)

## Project Structure

### Knowledge Base

`kb` is the place for the knowledge base source text files of your app. Put your **.scs** and **.gwf** files here.

### Problem Solver

`problem-solver` is the place for the problem solver of your app. Put your agents here. After changes in problem-solver you should rebuild it:
```sh
./scripts/build_problem_solver.sh
```

To enable DEBUG set fields in ostis-example-app.ini:

```sh
log_type = Console
log_file = sc-memory.log
log_level = Debug
```

### Interface

`interface` is the place for your interface modules.

To learn more about creating web components for the new web interface version follow this [link](https://github.com/MikhailSadovsky/sc-machine/tree/example/web/client)

### Scripts

`scripts` is the place for scripts files of your app. There are a few scripts already:

* build_problem_solver.sh [-f, --full]

Build the problem-solver of your app. Use an argument *-f* or *--full* for a complete rebuild of the problem-solver with the deleting of the *ostis-web-platform/sc-machine/bin* and *ostis-web-platform/sc-machine/build* folders.

* install_submodules.sh

Install or update the OSTIS platform.

## Author

* GitHub: [@ostis-apps](https://github.com/ostis-apps), [@ostis-ai](https://github.com/ostis-ai)

## Show your support

Give us a ⭐️ if you've liked this project!

## 🤝 Contributing

Contributions, issues and feature requests are welcome!<br />Feel free to check [issues page](https://github.com/ostis-apps/ostis-legislation/issues). 

## 📝 License

This project is [MIT](https://opensource.org/license/mit/) licensed.
