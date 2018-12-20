(ns mine-sweep.components.cell.styles
  (:require [mine-sweep.common.styles :as cstyles]
            [mine-sweep.common.constants :as const]))

(defn cell
  [investigating? state value]
  (cstyles/with-convex {:height           (cstyles/num->px const/cell-size)
                        :width            (cstyles/num->px const/cell-size)
                        :background-color cstyles/darkgrey-bg-color }))
