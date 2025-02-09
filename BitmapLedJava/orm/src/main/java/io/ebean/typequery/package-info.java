/*
 * Copyright (c) 2025 Lolosia
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions
 * of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO
 * THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE
 * OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

/**
 * Provides type safe query criteria support for Ebean ORM queries.
 * <p>
 *   'Query beans' like QCustomer are generated using the <code>avaje-ebeanorm-typequery-generator</code>
 *   for each entity bean type and can then be used to build queries with type safe criteria.
 * </p>
 *
 * <h2>Example - usage of QCustomer</h2>
 * <pre>{@code
 *
 *    Date fiveDaysAgo = ...
 *
 *    List<Customer> customers =
 *        new QCustomer()
 *
 *          // name is a known property of type string so
 *          // it has relevant expressions such as like, startsWith etc
 *          .name.ilike("rob")
 *
 *          // status is a specific Enum type is equalTo() in() etc
 *          .status.equalTo(Customer.Status.GOOD)
 *
 *          // registered is a date type with after(), before() etc
 *          .registered.after(fiveDaysAgo)
 *
 *          // contacts is an associated bean containing specific
 *          // properties and in this case we use email which is a string type
 *          .contacts.email.endsWith("@foo.com")
 *
 *          .orderBy()
 *            .name.asc()
 *            .registered.desc()
 *          .findList();
 *
 * }</pre>
 */
package io.ebean.typequery;