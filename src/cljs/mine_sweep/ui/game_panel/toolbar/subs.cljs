(ns mine-sweep.ui.game-panel.toolbar.subs
  (:require [re-frame.core :as rf]))

(rf/reg-sub
 :ui.game.tb/selected-level
 (fn [db _]
   (:current-level db)))
