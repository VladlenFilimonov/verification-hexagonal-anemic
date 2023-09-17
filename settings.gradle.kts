rootProject.name = "verification-hexagonal-anemic"

include("verification:verification-app")
include("verification:verification-in:verification-in-rest")
include("verification:verification-domain")
include("verification:verification-out:verification-database")
include("verification:verification-out:verification-cache")
include("verification:verification-out:verification-out-event")
include("verification:verification-itest")
include("commons")
