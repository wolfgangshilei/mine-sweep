(ns mine-sweep.runner
    (:require [doo.runner :refer-macros [doo-tests]]
              [mine-sweep.core-test]))

(doo-tests 'mine-sweep.core-test)
