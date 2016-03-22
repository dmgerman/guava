begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2011 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the License  * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express  * or implied. See the License for the specific language governing permissions and limitations under  * the License.  */
end_comment

begin_package
DECL|package|com.google.common.net
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|net
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|annotations
operator|.
name|Beta
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|annotations
operator|.
name|GwtCompatible
import|;
end_import

begin_comment
comment|/**  * Contains constant definitions for the HTTP header field names. See:  *<ul>  *<li><a href="http://www.ietf.org/rfc/rfc2109.txt">RFC 2109</a>  *<li><a href="http://www.ietf.org/rfc/rfc2183.txt">RFC 2183</a>  *<li><a href="http://www.ietf.org/rfc/rfc2616.txt">RFC 2616</a>  *<li><a href="http://www.ietf.org/rfc/rfc2965.txt">RFC 2965</a>  *<li><a href="http://www.ietf.org/rfc/rfc5988.txt">RFC 5988</a>  *</ul>  *  *  * @author Kurt Alfred Kluever  * @since 11.0  */
end_comment

begin_class
annotation|@
name|GwtCompatible
DECL|class|HttpHeaders
specifier|public
specifier|final
class|class
name|HttpHeaders
block|{
DECL|method|HttpHeaders ()
specifier|private
name|HttpHeaders
parameter_list|()
block|{}
comment|// HTTP Request and Response header fields
comment|/** The HTTP {@code Cache-Control} header field name. */
DECL|field|CACHE_CONTROL
specifier|public
specifier|static
specifier|final
name|String
name|CACHE_CONTROL
init|=
literal|"Cache-Control"
decl_stmt|;
comment|/** The HTTP {@code Content-Length} header field name. */
DECL|field|CONTENT_LENGTH
specifier|public
specifier|static
specifier|final
name|String
name|CONTENT_LENGTH
init|=
literal|"Content-Length"
decl_stmt|;
comment|/** The HTTP {@code Content-Type} header field name. */
DECL|field|CONTENT_TYPE
specifier|public
specifier|static
specifier|final
name|String
name|CONTENT_TYPE
init|=
literal|"Content-Type"
decl_stmt|;
comment|/** The HTTP {@code Date} header field name. */
DECL|field|DATE
specifier|public
specifier|static
specifier|final
name|String
name|DATE
init|=
literal|"Date"
decl_stmt|;
comment|/** The HTTP {@code Pragma} header field name. */
DECL|field|PRAGMA
specifier|public
specifier|static
specifier|final
name|String
name|PRAGMA
init|=
literal|"Pragma"
decl_stmt|;
comment|/** The HTTP {@code Via} header field name. */
DECL|field|VIA
specifier|public
specifier|static
specifier|final
name|String
name|VIA
init|=
literal|"Via"
decl_stmt|;
comment|/** The HTTP {@code Warning} header field name. */
DECL|field|WARNING
specifier|public
specifier|static
specifier|final
name|String
name|WARNING
init|=
literal|"Warning"
decl_stmt|;
comment|// HTTP Request header fields
comment|/** The HTTP {@code Accept} header field name. */
DECL|field|ACCEPT
specifier|public
specifier|static
specifier|final
name|String
name|ACCEPT
init|=
literal|"Accept"
decl_stmt|;
comment|/** The HTTP {@code Accept-Charset} header field name. */
DECL|field|ACCEPT_CHARSET
specifier|public
specifier|static
specifier|final
name|String
name|ACCEPT_CHARSET
init|=
literal|"Accept-Charset"
decl_stmt|;
comment|/** The HTTP {@code Accept-Encoding} header field name. */
DECL|field|ACCEPT_ENCODING
specifier|public
specifier|static
specifier|final
name|String
name|ACCEPT_ENCODING
init|=
literal|"Accept-Encoding"
decl_stmt|;
comment|/** The HTTP {@code Accept-Language} header field name. */
DECL|field|ACCEPT_LANGUAGE
specifier|public
specifier|static
specifier|final
name|String
name|ACCEPT_LANGUAGE
init|=
literal|"Accept-Language"
decl_stmt|;
comment|/** The HTTP {@code Access-Control-Request-Headers} header field name. */
DECL|field|ACCESS_CONTROL_REQUEST_HEADERS
specifier|public
specifier|static
specifier|final
name|String
name|ACCESS_CONTROL_REQUEST_HEADERS
init|=
literal|"Access-Control-Request-Headers"
decl_stmt|;
comment|/** The HTTP {@code Access-Control-Request-Method} header field name. */
DECL|field|ACCESS_CONTROL_REQUEST_METHOD
specifier|public
specifier|static
specifier|final
name|String
name|ACCESS_CONTROL_REQUEST_METHOD
init|=
literal|"Access-Control-Request-Method"
decl_stmt|;
comment|/** The HTTP {@code Authorization} header field name. */
DECL|field|AUTHORIZATION
specifier|public
specifier|static
specifier|final
name|String
name|AUTHORIZATION
init|=
literal|"Authorization"
decl_stmt|;
comment|/** The HTTP {@code Connection} header field name. */
DECL|field|CONNECTION
specifier|public
specifier|static
specifier|final
name|String
name|CONNECTION
init|=
literal|"Connection"
decl_stmt|;
comment|/** The HTTP {@code Cookie} header field name. */
DECL|field|COOKIE
specifier|public
specifier|static
specifier|final
name|String
name|COOKIE
init|=
literal|"Cookie"
decl_stmt|;
comment|/** The HTTP {@code Expect} header field name. */
DECL|field|EXPECT
specifier|public
specifier|static
specifier|final
name|String
name|EXPECT
init|=
literal|"Expect"
decl_stmt|;
comment|/** The HTTP {@code From} header field name. */
DECL|field|FROM
specifier|public
specifier|static
specifier|final
name|String
name|FROM
init|=
literal|"From"
decl_stmt|;
comment|/**    * The HTTP {@code Follow-Only-When-Prerender-Shown}</a> header field name.    *    * @since 17.0    */
annotation|@
name|Beta
DECL|field|FOLLOW_ONLY_WHEN_PRERENDER_SHOWN
specifier|public
specifier|static
specifier|final
name|String
name|FOLLOW_ONLY_WHEN_PRERENDER_SHOWN
init|=
literal|"Follow-Only-When-Prerender-Shown"
decl_stmt|;
comment|/** The HTTP {@code Host} header field name. */
DECL|field|HOST
specifier|public
specifier|static
specifier|final
name|String
name|HOST
init|=
literal|"Host"
decl_stmt|;
comment|/** The HTTP {@code If-Match} header field name. */
DECL|field|IF_MATCH
specifier|public
specifier|static
specifier|final
name|String
name|IF_MATCH
init|=
literal|"If-Match"
decl_stmt|;
comment|/** The HTTP {@code If-Modified-Since} header field name. */
DECL|field|IF_MODIFIED_SINCE
specifier|public
specifier|static
specifier|final
name|String
name|IF_MODIFIED_SINCE
init|=
literal|"If-Modified-Since"
decl_stmt|;
comment|/** The HTTP {@code If-None-Match} header field name. */
DECL|field|IF_NONE_MATCH
specifier|public
specifier|static
specifier|final
name|String
name|IF_NONE_MATCH
init|=
literal|"If-None-Match"
decl_stmt|;
comment|/** The HTTP {@code If-Range} header field name. */
DECL|field|IF_RANGE
specifier|public
specifier|static
specifier|final
name|String
name|IF_RANGE
init|=
literal|"If-Range"
decl_stmt|;
comment|/** The HTTP {@code If-Unmodified-Since} header field name. */
DECL|field|IF_UNMODIFIED_SINCE
specifier|public
specifier|static
specifier|final
name|String
name|IF_UNMODIFIED_SINCE
init|=
literal|"If-Unmodified-Since"
decl_stmt|;
comment|/** The HTTP {@code Last-Event-ID} header field name. */
DECL|field|LAST_EVENT_ID
specifier|public
specifier|static
specifier|final
name|String
name|LAST_EVENT_ID
init|=
literal|"Last-Event-ID"
decl_stmt|;
comment|/** The HTTP {@code Max-Forwards} header field name. */
DECL|field|MAX_FORWARDS
specifier|public
specifier|static
specifier|final
name|String
name|MAX_FORWARDS
init|=
literal|"Max-Forwards"
decl_stmt|;
comment|/** The HTTP {@code Origin} header field name. */
DECL|field|ORIGIN
specifier|public
specifier|static
specifier|final
name|String
name|ORIGIN
init|=
literal|"Origin"
decl_stmt|;
comment|/** The HTTP {@code Proxy-Authorization} header field name. */
DECL|field|PROXY_AUTHORIZATION
specifier|public
specifier|static
specifier|final
name|String
name|PROXY_AUTHORIZATION
init|=
literal|"Proxy-Authorization"
decl_stmt|;
comment|/** The HTTP {@code Range} header field name. */
DECL|field|RANGE
specifier|public
specifier|static
specifier|final
name|String
name|RANGE
init|=
literal|"Range"
decl_stmt|;
comment|/** The HTTP {@code Referer} header field name. */
DECL|field|REFERER
specifier|public
specifier|static
specifier|final
name|String
name|REFERER
init|=
literal|"Referer"
decl_stmt|;
comment|/**    * The HTTP<a href="https://www.w3.org/TR/service-workers/#update-algorithm">    * {@code Service-Worker}</a> header field name.    */
DECL|field|SERVICE_WORKER
specifier|public
specifier|static
specifier|final
name|String
name|SERVICE_WORKER
init|=
literal|"Service-Worker"
decl_stmt|;
comment|/** The HTTP {@code TE} header field name. */
DECL|field|TE
specifier|public
specifier|static
specifier|final
name|String
name|TE
init|=
literal|"TE"
decl_stmt|;
comment|/** The HTTP {@code Upgrade} header field name. */
DECL|field|UPGRADE
specifier|public
specifier|static
specifier|final
name|String
name|UPGRADE
init|=
literal|"Upgrade"
decl_stmt|;
comment|/** The HTTP {@code User-Agent} header field name. */
DECL|field|USER_AGENT
specifier|public
specifier|static
specifier|final
name|String
name|USER_AGENT
init|=
literal|"User-Agent"
decl_stmt|;
comment|// HTTP Response header fields
comment|/** The HTTP {@code Accept-Ranges} header field name. */
DECL|field|ACCEPT_RANGES
specifier|public
specifier|static
specifier|final
name|String
name|ACCEPT_RANGES
init|=
literal|"Accept-Ranges"
decl_stmt|;
comment|/** The HTTP {@code Access-Control-Allow-Headers} header field name. */
DECL|field|ACCESS_CONTROL_ALLOW_HEADERS
specifier|public
specifier|static
specifier|final
name|String
name|ACCESS_CONTROL_ALLOW_HEADERS
init|=
literal|"Access-Control-Allow-Headers"
decl_stmt|;
comment|/** The HTTP {@code Access-Control-Allow-Methods} header field name. */
DECL|field|ACCESS_CONTROL_ALLOW_METHODS
specifier|public
specifier|static
specifier|final
name|String
name|ACCESS_CONTROL_ALLOW_METHODS
init|=
literal|"Access-Control-Allow-Methods"
decl_stmt|;
comment|/** The HTTP {@code Access-Control-Allow-Origin} header field name. */
DECL|field|ACCESS_CONTROL_ALLOW_ORIGIN
specifier|public
specifier|static
specifier|final
name|String
name|ACCESS_CONTROL_ALLOW_ORIGIN
init|=
literal|"Access-Control-Allow-Origin"
decl_stmt|;
comment|/** The HTTP {@code Access-Control-Allow-Credentials} header field name. */
DECL|field|ACCESS_CONTROL_ALLOW_CREDENTIALS
specifier|public
specifier|static
specifier|final
name|String
name|ACCESS_CONTROL_ALLOW_CREDENTIALS
init|=
literal|"Access-Control-Allow-Credentials"
decl_stmt|;
comment|/** The HTTP {@code Access-Control-Expose-Headers} header field name. */
DECL|field|ACCESS_CONTROL_EXPOSE_HEADERS
specifier|public
specifier|static
specifier|final
name|String
name|ACCESS_CONTROL_EXPOSE_HEADERS
init|=
literal|"Access-Control-Expose-Headers"
decl_stmt|;
comment|/** The HTTP {@code Access-Control-Max-Age} header field name. */
DECL|field|ACCESS_CONTROL_MAX_AGE
specifier|public
specifier|static
specifier|final
name|String
name|ACCESS_CONTROL_MAX_AGE
init|=
literal|"Access-Control-Max-Age"
decl_stmt|;
comment|/** The HTTP {@code Age} header field name. */
DECL|field|AGE
specifier|public
specifier|static
specifier|final
name|String
name|AGE
init|=
literal|"Age"
decl_stmt|;
comment|/** The HTTP {@code Allow} header field name. */
DECL|field|ALLOW
specifier|public
specifier|static
specifier|final
name|String
name|ALLOW
init|=
literal|"Allow"
decl_stmt|;
comment|/** The HTTP {@code Content-Disposition} header field name. */
DECL|field|CONTENT_DISPOSITION
specifier|public
specifier|static
specifier|final
name|String
name|CONTENT_DISPOSITION
init|=
literal|"Content-Disposition"
decl_stmt|;
comment|/** The HTTP {@code Content-Encoding} header field name. */
DECL|field|CONTENT_ENCODING
specifier|public
specifier|static
specifier|final
name|String
name|CONTENT_ENCODING
init|=
literal|"Content-Encoding"
decl_stmt|;
comment|/** The HTTP {@code Content-Language} header field name. */
DECL|field|CONTENT_LANGUAGE
specifier|public
specifier|static
specifier|final
name|String
name|CONTENT_LANGUAGE
init|=
literal|"Content-Language"
decl_stmt|;
comment|/** The HTTP {@code Content-Location} header field name. */
DECL|field|CONTENT_LOCATION
specifier|public
specifier|static
specifier|final
name|String
name|CONTENT_LOCATION
init|=
literal|"Content-Location"
decl_stmt|;
comment|/** The HTTP {@code Content-MD5} header field name. */
DECL|field|CONTENT_MD5
specifier|public
specifier|static
specifier|final
name|String
name|CONTENT_MD5
init|=
literal|"Content-MD5"
decl_stmt|;
comment|/** The HTTP {@code Content-Range} header field name. */
DECL|field|CONTENT_RANGE
specifier|public
specifier|static
specifier|final
name|String
name|CONTENT_RANGE
init|=
literal|"Content-Range"
decl_stmt|;
comment|/**    * The HTTP<a href="http://w3.org/TR/CSP/#content-security-policy-header-field">    * {@code Content-Security-Policy}</a> header field name.    *    * @since 15.0    */
DECL|field|CONTENT_SECURITY_POLICY
specifier|public
specifier|static
specifier|final
name|String
name|CONTENT_SECURITY_POLICY
init|=
literal|"Content-Security-Policy"
decl_stmt|;
comment|/**    * The HTTP<a href="http://w3.org/TR/CSP/#content-security-policy-report-only-header-field">    * {@code Content-Security-Policy-Report-Only}</a> header field name.    *    * @since 15.0    */
DECL|field|CONTENT_SECURITY_POLICY_REPORT_ONLY
specifier|public
specifier|static
specifier|final
name|String
name|CONTENT_SECURITY_POLICY_REPORT_ONLY
init|=
literal|"Content-Security-Policy-Report-Only"
decl_stmt|;
comment|/** The HTTP {@code ETag} header field name. */
DECL|field|ETAG
specifier|public
specifier|static
specifier|final
name|String
name|ETAG
init|=
literal|"ETag"
decl_stmt|;
comment|/** The HTTP {@code Expires} header field name. */
DECL|field|EXPIRES
specifier|public
specifier|static
specifier|final
name|String
name|EXPIRES
init|=
literal|"Expires"
decl_stmt|;
comment|/** The HTTP {@code Last-Modified} header field name. */
DECL|field|LAST_MODIFIED
specifier|public
specifier|static
specifier|final
name|String
name|LAST_MODIFIED
init|=
literal|"Last-Modified"
decl_stmt|;
comment|/** The HTTP {@code Link} header field name. */
DECL|field|LINK
specifier|public
specifier|static
specifier|final
name|String
name|LINK
init|=
literal|"Link"
decl_stmt|;
comment|/** The HTTP {@code Location} header field name. */
DECL|field|LOCATION
specifier|public
specifier|static
specifier|final
name|String
name|LOCATION
init|=
literal|"Location"
decl_stmt|;
comment|/** The HTTP {@code P3P} header field name. Limited browser support. */
DECL|field|P3P
specifier|public
specifier|static
specifier|final
name|String
name|P3P
init|=
literal|"P3P"
decl_stmt|;
comment|/** The HTTP {@code Proxy-Authenticate} header field name. */
DECL|field|PROXY_AUTHENTICATE
specifier|public
specifier|static
specifier|final
name|String
name|PROXY_AUTHENTICATE
init|=
literal|"Proxy-Authenticate"
decl_stmt|;
comment|/** The HTTP {@code Refresh} header field name. Non-standard header supported by most browsers. */
DECL|field|REFRESH
specifier|public
specifier|static
specifier|final
name|String
name|REFRESH
init|=
literal|"Refresh"
decl_stmt|;
comment|/** The HTTP {@code Retry-After} header field name. */
DECL|field|RETRY_AFTER
specifier|public
specifier|static
specifier|final
name|String
name|RETRY_AFTER
init|=
literal|"Retry-After"
decl_stmt|;
comment|/** The HTTP {@code Server} header field name. */
DECL|field|SERVER
specifier|public
specifier|static
specifier|final
name|String
name|SERVER
init|=
literal|"Server"
decl_stmt|;
comment|/**    * The HTTP<a href="https://www.w3.org/TR/service-workers/#update-algorithm">    * {@code Service-Worker-Allowed}</a> header field name.    *    * @since 20.0    */
DECL|field|SERVICE_WORKER_ALLOWED
specifier|public
specifier|static
specifier|final
name|String
name|SERVICE_WORKER_ALLOWED
init|=
literal|"Service-Worker-Allowed"
decl_stmt|;
comment|/** The HTTP {@code Set-Cookie} header field name. */
DECL|field|SET_COOKIE
specifier|public
specifier|static
specifier|final
name|String
name|SET_COOKIE
init|=
literal|"Set-Cookie"
decl_stmt|;
comment|/** The HTTP {@code Set-Cookie2} header field name. */
DECL|field|SET_COOKIE2
specifier|public
specifier|static
specifier|final
name|String
name|SET_COOKIE2
init|=
literal|"Set-Cookie2"
decl_stmt|;
comment|/**    * The HTTP    *<a href="http://tools.ietf.org/html/rfc6797#section-6.1">{@code Strict-Transport-Security}</a>    * header field name.    *    * @since 15.0    */
DECL|field|STRICT_TRANSPORT_SECURITY
specifier|public
specifier|static
specifier|final
name|String
name|STRICT_TRANSPORT_SECURITY
init|=
literal|"Strict-Transport-Security"
decl_stmt|;
comment|/**    * The HTTP<a href="http://www.w3.org/TR/resource-timing/#cross-origin-resources">    * {@code Timing-Allow-Origin}</a> header field name.    *    * @since 15.0    */
DECL|field|TIMING_ALLOW_ORIGIN
specifier|public
specifier|static
specifier|final
name|String
name|TIMING_ALLOW_ORIGIN
init|=
literal|"Timing-Allow-Origin"
decl_stmt|;
comment|/** The HTTP {@code Trailer} header field name. */
DECL|field|TRAILER
specifier|public
specifier|static
specifier|final
name|String
name|TRAILER
init|=
literal|"Trailer"
decl_stmt|;
comment|/** The HTTP {@code Transfer-Encoding} header field name. */
DECL|field|TRANSFER_ENCODING
specifier|public
specifier|static
specifier|final
name|String
name|TRANSFER_ENCODING
init|=
literal|"Transfer-Encoding"
decl_stmt|;
comment|/** The HTTP {@code Vary} header field name. */
DECL|field|VARY
specifier|public
specifier|static
specifier|final
name|String
name|VARY
init|=
literal|"Vary"
decl_stmt|;
comment|/** The HTTP {@code WWW-Authenticate} header field name. */
DECL|field|WWW_AUTHENTICATE
specifier|public
specifier|static
specifier|final
name|String
name|WWW_AUTHENTICATE
init|=
literal|"WWW-Authenticate"
decl_stmt|;
comment|// Common, non-standard HTTP header fields
comment|/** The HTTP {@code DNT} header field name. */
DECL|field|DNT
specifier|public
specifier|static
specifier|final
name|String
name|DNT
init|=
literal|"DNT"
decl_stmt|;
comment|/** The HTTP {@code X-Content-Type-Options} header field name. */
DECL|field|X_CONTENT_TYPE_OPTIONS
specifier|public
specifier|static
specifier|final
name|String
name|X_CONTENT_TYPE_OPTIONS
init|=
literal|"X-Content-Type-Options"
decl_stmt|;
comment|/** The HTTP {@code X-Do-Not-Track} header field name. */
DECL|field|X_DO_NOT_TRACK
specifier|public
specifier|static
specifier|final
name|String
name|X_DO_NOT_TRACK
init|=
literal|"X-Do-Not-Track"
decl_stmt|;
comment|/** The HTTP {@code X-Forwarded-For} header field name. */
DECL|field|X_FORWARDED_FOR
specifier|public
specifier|static
specifier|final
name|String
name|X_FORWARDED_FOR
init|=
literal|"X-Forwarded-For"
decl_stmt|;
comment|/** The HTTP {@code X-Forwarded-Proto} header field name. */
DECL|field|X_FORWARDED_PROTO
specifier|public
specifier|static
specifier|final
name|String
name|X_FORWARDED_PROTO
init|=
literal|"X-Forwarded-Proto"
decl_stmt|;
comment|/**    * The HTTP<a href="http://goo.gl/lQirAH">{@code X-Forwarded-Host}</a> header field name.    *    * @since 20.0    */
DECL|field|X_FORWARDED_HOST
specifier|public
specifier|static
specifier|final
name|String
name|X_FORWARDED_HOST
init|=
literal|"X-Forwarded-Host"
decl_stmt|;
comment|/**    * The HTTP<a href="http://goo.gl/YtV2at">{@code X-Forwarded-Port}</a> header field name.    *    * @since 20.0    */
DECL|field|X_FORWARDED_PORT
specifier|public
specifier|static
specifier|final
name|String
name|X_FORWARDED_PORT
init|=
literal|"X-Forwarded-Port"
decl_stmt|;
comment|/** The HTTP {@code X-Frame-Options} header field name. */
DECL|field|X_FRAME_OPTIONS
specifier|public
specifier|static
specifier|final
name|String
name|X_FRAME_OPTIONS
init|=
literal|"X-Frame-Options"
decl_stmt|;
comment|/** The HTTP {@code X-Powered-By} header field name. */
DECL|field|X_POWERED_BY
specifier|public
specifier|static
specifier|final
name|String
name|X_POWERED_BY
init|=
literal|"X-Powered-By"
decl_stmt|;
comment|/**    * The HTTP    *<a href="http://tools.ietf.org/html/draft-evans-palmer-key-pinning">{@code Public-Key-Pins}</a>    * header field name.    *    * @since 15.0    */
DECL|field|PUBLIC_KEY_PINS
annotation|@
name|Beta
specifier|public
specifier|static
specifier|final
name|String
name|PUBLIC_KEY_PINS
init|=
literal|"Public-Key-Pins"
decl_stmt|;
comment|/**    * The HTTP<a href="http://tools.ietf.org/html/draft-evans-palmer-key-pinning">    * {@code Public-Key-Pins-Report-Only}</a> header field name.    *    * @since 15.0    */
DECL|field|PUBLIC_KEY_PINS_REPORT_ONLY
annotation|@
name|Beta
specifier|public
specifier|static
specifier|final
name|String
name|PUBLIC_KEY_PINS_REPORT_ONLY
init|=
literal|"Public-Key-Pins-Report-Only"
decl_stmt|;
comment|/** The HTTP {@code X-Requested-With} header field name. */
DECL|field|X_REQUESTED_WITH
specifier|public
specifier|static
specifier|final
name|String
name|X_REQUESTED_WITH
init|=
literal|"X-Requested-With"
decl_stmt|;
comment|/** The HTTP {@code X-User-IP} header field name. */
DECL|field|X_USER_IP
specifier|public
specifier|static
specifier|final
name|String
name|X_USER_IP
init|=
literal|"X-User-IP"
decl_stmt|;
comment|/** The HTTP {@code X-XSS-Protection} header field name. */
DECL|field|X_XSS_PROTECTION
specifier|public
specifier|static
specifier|final
name|String
name|X_XSS_PROTECTION
init|=
literal|"X-XSS-Protection"
decl_stmt|;
comment|/**    * The HTTP<a href="http://html.spec.whatwg.org/multipage/semantics.html#hyperlink-auditing">    * {@code Ping-From}</a> header field name.    *    * @since 19.0    */
DECL|field|PING_FROM
specifier|public
specifier|static
specifier|final
name|String
name|PING_FROM
init|=
literal|"Ping-From"
decl_stmt|;
comment|/**    * The HTTP<a href="http://html.spec.whatwg.org/multipage/semantics.html#hyperlink-auditing">    * {@code Ping-To}</a> header field name.    *    * @since 19.0    */
DECL|field|PING_TO
specifier|public
specifier|static
specifier|final
name|String
name|PING_TO
init|=
literal|"Ping-To"
decl_stmt|;
block|}
end_class

end_unit

