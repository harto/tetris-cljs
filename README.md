# Tetris

A minimal ClojureScript implementation of Tetris.

## Setup

To get an interactive development environment: run:

    lein figwheel

and open your browser at [localhost:3449](http://localhost:3449/).

To connect to the environment from Emacs/Cider, run:

    (cider-connect "localhost" 7888)

Then start a Figwheel REPL:

    (figwheel-sidecar.repl-api/cljs-repl)
