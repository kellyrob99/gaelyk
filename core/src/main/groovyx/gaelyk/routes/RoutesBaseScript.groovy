/*
 * Copyright 2009-2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package groovyx.gaelyk.routes

/**
 * Base script class used for evaluating the routes.
 * 
 * @author Guillaume Laforge
 */
abstract class RoutesBaseScript extends Script {
    /** The list of routes available */
    List<Route> routes = []

    def all    (Map m, String route) { handle m, route, HttpMethod.ALL }
    def get    (Map m, String route) { handle m, route, HttpMethod.GET }
    def post   (Map m, String route) { handle m, route, HttpMethod.POST }
    def put    (Map m, String route) { handle m, route, HttpMethod.PUT }
    def delete (Map m, String route) { handle m, route, HttpMethod.DELETE }

    def email  (Map m) {
        routes << new Route("/_ah/mail/*", m.to,
                HttpMethod.POST, RedirectionType.FORWARD,
                null, null, 0, false, true)
    }

    def jabber (Map m) {
        routes << new Route("/_ah/xmpp/message/chat/", m.to,
                HttpMethod.POST, RedirectionType.FORWARD,
                null, null, 0, false, false, true)
    }

    /**
     * Handle all routes.
     *
     * @param m a map containing the forward or redirect location,
     * as well as potential validation rules for the variables appearing in the route,
     * a definition of a caching duration, and the ability to ignore certain paths
     * like GAE's /_ah/* special URLs.
     */
    protected handle(Map m, String route, HttpMethod method) {
        RedirectionType redirectionType = m.forward ? RedirectionType.FORWARD : RedirectionType.REDIRECT

        def destination = m.forward ?: m.redirect
        def validator = m.validate ?: null
        def cacheExpiration = m.cache ?: 0
        def ignore = m.ignore ?: false
        def ns = m.namespace ?: null

        routes << new Route(route, destination, method, redirectionType, validator, ns, cacheExpiration, ignore)
    }
}