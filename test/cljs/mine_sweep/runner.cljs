(ns mine-sweep.runner
    (:require [doo.runner :refer-macros [doo-tests]]
              [mine-sweep.core-test]
              [mine-sweep.ui.game-panel.mine-field.events-test]))

(set! goog.DEBUG false)

(doo-tests 'mine-sweep.core-test
           'mine-sweep.ui.game-panel.mine-field.events-test)
