(ns mine-sweep.db
  (:require [cljs.spec.alpha :as spec]
            [mine-sweep.ui.game-panel.db]))

(spec/def ::db (spec/keys :req-un [:ui.game/current-level
                                   :ui.game/mine-field
                                   :ui.game/game-state]
                          :req    [:ui.game/mouse-event
                                   :ui.game/timer
                                   :ui.game.mf/hit-mine?]
                          :opt    [:ui.game.mf/non-mine-cells-count
                                   :ui.game.mf/revealed-cells-pos]))

(def default-db
  {:name                "mine-sweep"
   :current-level       :easy
   ;; :ui.game
   :mine-field          {}
   :game-state          :reset
   :ui.game/mouse-event nil
   :ui.game/timer       0
   :ui.game.mf/hit-mine?   false})
