(ns react-hmr.refresh
  "An example implementation of a macro that registers and updates React
  components with the new hot-loading functionality, \"React Fresh\"."
  (:require [cljs-bean.core :refer [bean]]
            ["react" :as react]
            ["react-refresh/runtime" :as react-refresh]
            [goog.object :as gobj])
  (:require-macros [react-hmr.refresh]))

(def ->props bean)

(def memo react/memo)

(defonce registered-components
  (atom {}))

(defn register!
  "Registers a component with the React Fresh runtime.
  `type` is the component function, and `id` is the unique ID assigned to it
  (e.g. component name) for cache invalidation."
  [type id]
  (swap! registered-components assoc type id)
  (react-refresh/register type id))


(defn reload []
  (js/console.log (react-refresh/performReactRefresh)))

(def signature! react-refresh/createSignatureFunctionForTransform)

;; Inject the refresh runtime
(react-refresh/injectIntoGlobalHook js/window)
