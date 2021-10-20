use teloxide::{prelude::*, types::InlineQuery, Bot};
use tokio_stream::wrappers::UnboundedReceiverStream;

pub async fn inline_query_handler(rx: DispatcherHandlerRx<Bot, InlineQuery>) {
    UnboundedReceiverStream::new(rx)
        .for_each_concurrent(None, |req| async move {
            let update = req.update;
            let user = update.from;

            let fullname = user.full_name();
            let username = user.username.map(|s| format!("@{}", s));
            let displayname = username.unwrap_or(fullname);

            log::info!(
                "Got inline query from {} ({}): {}",
                displayname,
                user.id,
                update.query
            );

            let results = vec![];

            // Send it off! One thing to note -- the ID we use here must be of the query we're responding to.
            let response = req
                .requester
                .answer_inline_query(&update.id, results)
                .send()
                .await;
            if let Err(err) = response {
                log::error!("Error in handler: {:?}", err);
            }
        })
        .await;
}
