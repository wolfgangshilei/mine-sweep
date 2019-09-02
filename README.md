# mine-sweep

A [re-frame](https://github.com/Day8/re-frame) application designed to ... well, that part is up to you.


### Install shadow-cljs:

```
npm install --save-dev shadow-cljs
npm install -g shadow-cljs
```

For more information about shadow-cljs, please read the document of [shadow-cljs](https://shadow-cljs.github.io/docs/UsersGuide.html#_command_line).

## Development Mode

### Start Cider from Emacs:

Put this in your Emacs config file:

Start a shadow-cljs REPL with `cider-jack-in-clojurescript` or (`C-c M-J`)

```
Which command should be used:
```

enter shadow-cljs

```
Select ClojureScript REPL type:
```

enter shadow

Then enter "app" as the name of the build target.

### Run application:

```
shadow-cljs watch app
```

Figwheel will automatically push cljs changes to the browser.

Wait a bit, then browse to [http://localhost:8080](http://localhost:8080).

### Run tests:

Install karma and headless chrome (ignore this step for now)

```
    npm install -g karma-cli
    npm install karma karma-cljs-test karma-chrome-launcher --save-dev
```

And then run your tests

```
shadow-cljs watch test
```

## Production Build


To compile clojurescript to javascript:

```
shadow-cljs release app
```
