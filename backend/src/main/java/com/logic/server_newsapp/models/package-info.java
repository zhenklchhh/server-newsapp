/**
 * Package containing the data model classes for the application.
 *
 * <p>
 *   This package defines the structure of the data that the application
 *   operates on. These model classes typically correspond to database
 *   entities and are used for data transfer between different layers
 *   of the application. Key components include:
 *
 *   <ul>
 *     <li><em>User</em>: Represents a user of the application,
 *          including properties such as login, password, and role.</li>
 *     <li><em>News</em>: Represents a news item,
 *         including properties such as title, content, publish date.</li>
 *     <li><em>Community</em>: Represents a community where news is posted.</li>
 *     <li><em>Comments</em>: Represents a user comment on a news item.</li>
 *     <li><em>Other Entity classes:</em> If you have other model classes here,
 *        document them using the same format.
 *     </li>
 *   </ul>
 * </p>
 * <p>
 *  These model classes are typically simple POJOs (Plain Old Java Objects)
 *  and may be annotated with JPA annotations
 *  (such as `@Entity`, `@Table`, `@Id`, etc) to
 *  map them to database tables.
 * </p>
 */
package com.logic.server_newsapp.models;
