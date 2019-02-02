(ns mine-sweep.ui.game-panel.db
  (:require [cljs.spec.alpha :as spec]))

(spec/def ::state #{:covered :revealed :marked :exploded :investigating :wrongly-marked})
(spec/def ::mine? boolean?)
(spec/def ::pos (spec/cat :row (complement neg?) :column (complement neg?)))
(spec/def ::neighbour ::pos)
(spec/def ::neighbours (spec/&
                        (spec/*
                         (spec/spec (spec/cat :neighbour ::neighbour)))
                        #(<= 0 (count %) 8)))

(spec/def ::mouse-event-id #{:investigate
                             :main-btn-down
                             :main-btn-up
                             :aux-btn-down
                             :aux-btn-up
                             :enter
                             :leave
                             :ignore})

(spec/def ::mine-neighbour-count pos?)

(spec/def ::cell (spec/keys :req-un [::state ::mine? ::neighbours ::pos]
                            :opt-un [::mine-neighbour-count]))
#_(spec/def :ui.game.mine-field/mouse-events (spec/coll-of ::mouse-event :min-count 0 :max-count 2))

(spec/def :ui.game/mouse-event (spec/nilable (spec/cat :mouse-event-id ::mouse-event-id
                                                :cell (spec/spec ::cell))))

(spec/def :ui.game/mine-field (spec/map-of ::pos ::cell))
(spec/def :ui.game/game-state #{:ongoing :lose :win :reset})
(spec/def :ui.game/current-level #{:hard :easy :medium})

(spec/def :ui.game/timer (complement neg?))
(spec/def :ui.game.mf/hit-mine? boolean?)

(spec/def :ui.game.mf/non-mine-cells-count pos?)
(spec/def :ui.game.mf/revealed-cells-pos (spec/coll-of ::pos :kind set?))

#_(spec/conform ::pos [1 5])
#_(spec/conform ::mine? true)
#_(spec/explain ::neighbours [[1 8] [2 8] [1 8] [2 8] [1 8] [2 8] [1 8] [2 8]])

#_(spec/conform :ui.game/mine-field {[0 0] {:state :covered, :mine? false, :neighbours '([1 1] [1 0] [0 1]), :pos [0 0]},
                                     [0 1] {:state :covered, :mine? false, :neighbours '([1 2] [1 0] [1 1] [0 2] [0 0]), :pos [0 1]},
                                     [0 2] {:state :covered, :mine? false, :neighbours '([1 3] [1 1] [1 2] [0 3] [0 1]), :pos [0 2]},
                                     [0 3] {:state :covered, :mine? false, :neighbours '([1 4] [1 2] [1 3] [0 4] [0 2]), :pos [0 3]},
                                     [0 4] {:state :covered, :mine? false, :neighbours '([1 5] [1 3] [1 4] [0 5] [0 3]), :pos [0 4]}})
