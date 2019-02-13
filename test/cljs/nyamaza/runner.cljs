(ns nyamaza.runner
    (:require [doo.runner :refer-macros [doo-tests]]
              [nyamaza.core-test]))

(doo-tests 'nyamaza.core-test)
