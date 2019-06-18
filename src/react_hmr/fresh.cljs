(ns react-hmr.fresh
  "An example implementation of a macro that registers and updates React
  components with the new hot-loading functionality, \"React Fresh\"."
  (:require [cljs-bean.core :refer [bean]]
            ["react-refresh/runtime" :as react-fresh])
  (:require-macros [react-hmr.fresh]))

(def ->props bean)

(defn register
  "Registers a component with the React Fresh runtime.
  `type` is the component function, and `id` is the unique ID assigned to it
  (e.g. component name) for cache invalidation."
  [type id]
  (js/console.log type id)
  (react-fresh/register type id))

(defn signature [type key]
  (js/console.log type key))
