mod inline;

use inline::inline_query_handler;
use teloxide::prelude::*;
use tracing::info;
use tracing_subscriber::{self};

const VERSION: &'static str = env!("CARGO_PKG_VERSION");

#[tokio::main]
async fn main() {
    run().await;
}

async fn run() {
    // Load env variables from .env file
    dotenv::dotenv().ok();

    // Initialize tracing logging
    init_tracing();

    let bot = Bot::from_env();

    info!("Starting EmotesBot v{}", VERSION);

    Dispatcher::new(bot)
        .inline_queries_handler(inline_query_handler)
        .dispatch()
        .await;
}

#[cfg(debug_assertions)]
/// Initialize tracing logging for development
fn init_tracing() {
    tracing_subscriber::fmt().init();
}

#[cfg(not(debug_assertions))]
/// Initialize tracing logging for production
fn init_tracing() {
    tracing_subscriber::fmt().json().init();
}
