# NewRelic 筆記

```
提供企業加速雲端創新必備的APM監控工具。
New Relic的可觀測性分析機制及APM解決方案使應用程式及系統效能監控變得更容易。
```

## 目錄

- [NewRelic 筆記](#newrelic-筆記)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [指令](#指令)
- [設定檔](#設定檔)
- [PHP代理安裝：Ubuntu和Debian](#php代理安裝ubuntu和debian)
- [監控 Python](#監控-python)

## 參考資料

[官網](https://newrelic.com/)

[官方文檔](https://docs.newrelic.com/)

# 指令

```bash
# 使用 ini 執行newrelic
newrelic-admin generate-config '*** REPLACE ME ***' newrelic.ini
```

# 設定檔

[Python agent configuration](https://docs.newrelic.com/docs/apm/agents/python-agent/configuration/python-agent-configuration/)

```ini
# ---------------------------------------------------------------------------

#
# This file configures the New Relic Python Agent.
#
# The path to the configuration file should be supplied to the function
# newrelic.agent.initialize() when the agent is being initialized.
#
# The configuration file follows a structure similar to what you would
# find for Microsoft Windows INI files. For further information on the
# configuration file format see the Python ConfigParser documentation at:
#
#    http://docs.python.org/library/configparser.html
#
# For further discussion on the behaviour of the Python agent that can
# be configured via this configuration file see:
#
#    http://newrelic.com/docs/python/python-agent-configuration
#

# ---------------------------------------------------------------------------

# Here are the settings that are common to all environments.

[newrelic]

# You must specify the license key associated with your New
# Relic account. This key binds the Python Agent's data to your
# account in the New Relic service.
license_key = *** REPLACE ME ***

# The application name. Set this to be the name of your
# application as you would like it to show up in New Relic UI.
# The UI will then auto-map instances of your application into a
# entry on your home dashboard page.
app_name = Python Application

# When "true", the agent collects performance data about your
# application and reports this data to the New Relic UI at
# newrelic.com. This global switch is normally overridden for
# each environment below.
monitor_mode = true

# Sets the name of a file to log agent messages to. Useful for
# debugging any issues with the agent. This is not set by
# default as it is not known in advance what user your web
# application processes will run as and where they have
# permission to write to. Whatever you set this to you must
# ensure that the permissions for the containing directory and
# the file itself are correct, and that the user that your web
# application runs as can write to the file. If not able to
# write out a log file, it is also possible to say "stderr" and
# output to standard error output. This would normally result in
# output appearing in your web server log.
#log_file = /tmp/newrelic-python-agent.log

# Sets the level of detail of messages sent to the log file, if
# a log file location has been provided. Possible values, in
# increasing order of detail, are: "critical", "error", "warning",
# "info" and "debug". When reporting any agent issues to New
# Relic technical support, the most useful setting for the
# support engineers is "debug". However, this can generate a lot
# of information very quickly, so it is best not to keep the
# agent at this level for longer than it takes to reproduce the
# problem you are experiencing.
log_level = info

# The Python Agent communicates with the New Relic service using
# SSL by default. Note that this does result in an increase in
# CPU overhead, over and above what would occur for a non SSL
# connection, to perform the encryption involved in the SSL
# communication. This work is though done in a distinct thread
# to those handling your web requests, so it should not impact
# response times. You can if you wish revert to using a non SSL
# connection, but this will result in information being sent
# over a plain socket connection and will not be as secure.
ssl = true

# High Security Mode enforces certain security settings, and
# prevents them from being overridden, so that no sensitive data
# is sent to New Relic. Enabling High Security Mode means that
# SSL is turned on, request parameters are not collected, and SQL
# can not be sent to New Relic in its raw form. To activate High
# Security Mode, it must be set to 'true' in this local .ini
# configuration file AND be set to 'true' in the server-side
# configuration in the New Relic user interface. For details, see
# https://docs.newrelic.com/docs/subscriptions/high-security
high_security = false

# The Python Agent will attempt to connect directly to the New
# Relic service. If there is an intermediate firewall between
# your host and the New Relic service that requires you to use a
# HTTP proxy, then you should set both the "proxy_host" and
# "proxy_port" settings to the required values for the HTTP
# proxy. The "proxy_user" and "proxy_pass" settings should
# additionally be set if proxy authentication is implemented by
# the HTTP proxy. The "proxy_scheme" setting dictates what
# protocol scheme is used in talking to the HTTP proxy. This
# would normally always be set as "http" which will result in the
# agent then using a SSL tunnel through the HTTP proxy for end to
# end encryption.
# proxy_scheme = http
# proxy_host = hostname
# proxy_port = 8080
# proxy_user =
# proxy_pass =

# Capturing request parameters is off by default. To enable the
# capturing of request parameters, first ensure that the setting
# "attributes.enabled" is set to "true" (the default value), and
# then add "request.parameters.*" to the "attributes.include"
# setting. For details about attributes configuration, please
# consult the documentation.
# attributes.include = request.parameters.*

# The transaction tracer captures deep information about slow
# transactions and sends this to the UI on a periodic basis. The
# transaction tracer is enabled by default. Set this to "false"
# to turn it off.
transaction_tracer.enabled = true

# Threshold in seconds for when to collect a transaction trace.
# When the response time of a controller action exceeds this
# threshold, a transaction trace will be recorded and sent to
# the UI. Valid values are any positive float value, or (default)
# "apdex_f", which will use the threshold for a dissatisfying
# Apdex controller action - four times the Apdex T value.
transaction_tracer.transaction_threshold = apdex_f

# When the transaction tracer is on, SQL statements can
# optionally be recorded. The recorder has three modes, "off"
# which sends no SQL, "raw" which sends the SQL statement in its
# original form, and "obfuscated", which strips out numeric and
# string literals.
transaction_tracer.record_sql = obfuscated

# Threshold in seconds for when to collect stack trace for a SQL
# call. In other words, when SQL statements exceed this
# threshold, then capture and send to the UI the current stack
# trace. This is helpful for pinpointing where long SQL calls
# originate from in an application.
transaction_tracer.stack_trace_threshold = 0.5

# Determines whether the agent will capture query plans for slow
# SQL queries. Only supported in MySQL and PostgreSQL. Set this
# to "false" to turn it off.
transaction_tracer.explain_enabled = true

# Threshold for query execution time below which query plans
# will not not be captured. Relevant only when "explain_enabled"
# is true.
transaction_tracer.explain_threshold = 0.5

# Space separated list of function or method names in form
# 'module:function' or 'module:class.function' for which
# additional function timing instrumentation will be added.
transaction_tracer.function_trace =

# The error collector captures information about uncaught
# exceptions or logged exceptions and sends them to UI for
# viewing. The error collector is enabled by default. Set this
# to "false" to turn it off.
error_collector.enabled = true

# To stop specific errors from reporting to the UI, set this to
# a space separated list of the Python exception type names to
# ignore. The exception name should be of the form 'module:class'.
error_collector.ignore_errors =

# Browser monitoring is the Real User Monitoring feature of the UI.
# For those Python web frameworks that are supported, this
# setting enables the auto-insertion of the browser monitoring
# JavaScript fragments.
browser_monitoring.auto_instrument = true

# A thread profiling session can be scheduled via the UI when
# this option is enabled. The thread profiler will periodically
# capture a snapshot of the call stack for each active thread in
# the application to construct a statistically representative
# call tree.
thread_profiler.enabled = true

# ---------------------------------------------------------------------------

#
# The application environments. These are specific settings which
# override the common environment settings. The settings related to a
# specific environment will be used when the environment argument to the
# newrelic.agent.initialize() function has been defined to be either
# "development", "test", "staging" or "production".
#

[newrelic:development]
monitor_mode = false

[newrelic:test]
monitor_mode = false

[newrelic:staging]
app_name = Python Application (Staging)
monitor_mode = true

[newrelic:production]
monitor_mode = true

# ---------------------------------------------------------------------------
```

# PHP代理安裝：Ubuntu和Debian

[PHP agent installation: Ubuntu and Debian](https://docs.newrelic.com/docs/apm/agents/php-agent/installation/php-agent-installation-ubuntu-debian/)

```bash
# 安裝 newrelic-repo-5-3.noarch 擴展庫
sudo rpm -Uvh http://yum.newrelic.com/pub/newrelic/el5/x86_64/newrelic-repo-5-3.noarch.rpm
	# -h或--hash 套件安裝時列出標記。
	# -U<套件檔>或--upgrade<套件檔> 升級指定的套件檔。
	# -v 顯示指令執行過程。

# New Relic 的 PHP 代理的包名是newrelic-php5. 儘管包名引用了 PHP 5，但該包適用於所有受支持的 PHP 版本。
sudo yum install newrelic-php5 -y

# 上方指令出現
# Please run newrelic-install as root to complete installation.
# 非標準安裝：手動完成安裝。(需輸入 New Relic license key)
sudo newrelic-install install

# 找出 設定檔位置
php -i | grep newrelic.ini

# 編輯
vim /etc/opt/remi/php74/php.d/newrelic.ini
# newrelic.license = license_key
# app_name

# 起動
systemctl enable newrelic-daemon
```

# 監控 Python

```bash
newrelic-admin run-program {command}

# docker-compose
environment:
    - NEW_RELIC_CONFIG_FILE=/usr/src/app/newrelic-manager-dev-worker.ini

設定 newrelic ini
```
