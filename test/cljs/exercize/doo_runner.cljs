(ns exercize.doo-runner
  (:require [doo.runner :refer-macros [doo-tests]]
            [exercize.core-test]))

(doo-tests 'exercize.core-test)

