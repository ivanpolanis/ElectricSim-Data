#!/bin/sh
set -e

# Ensure Grafana owns its writable directories before starting.
if [ -d /var/lib/grafana ]; then
  chown -R 472:472 /var/lib/grafana
fi

if [ -d /etc/grafana/provisioning/dashboards ]; then
  chmod -R g+rwX,o+rwX /etc/grafana/provisioning/dashboards
fi

exec /run.sh "$@"
