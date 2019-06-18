(ns react-hmr.fresh
  "An example implementation of a macro that registers and updates React
  components with the new hot-loading functionality, \"React Fresh\"."
  (:require [clojure.walk]
            [clojure.string :as str]))

(defn find-all
  "Recursively walks a tree structure and finds all elements
  that match `pred`. Returns a vector of results."
  [pred tree]
  (let [results (atom [])]
    (clojure.walk/postwalk
     (fn walker [x]
       (when (pred x)
         (swap! results conj x))
       x)
     tree)
    @results))

(defn hook? [x]
  (when (list? x)
    (let [fst (first x)]
      (and (symbol? fst) (str/starts-with? (name fst)
                                           "use")))))

(defmacro defnc
  "Defines a new React component."
  [display-name props-bindings & body]
  (let [usables (find-all hook?
                          body)]
    `(do
       (defn ~display-name [props#]
         (let [~props-bindings [(->props props#)]] ;; `props` is a proxy to `bean`
           ~@body))
       (signature ~display-name (list ~@(map str usables)))
       (register ~display-name ~(str display-name)))))
