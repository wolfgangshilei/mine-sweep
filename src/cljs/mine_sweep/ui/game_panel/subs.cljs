(ns mine-sweep.ui.game-panel.subs
  (:require [re-frame.core :as rf]))

(rf/reg-sub
 :ui.game/current-level
 (fn [db _]
   (:ui.game/current-level db)))
